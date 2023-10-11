package plugway.mc.music.disc.dj.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.sfrei.tracksearch.clients.setup.TrackSource;
import io.sfrei.tracksearch.tracks.SoundCloudTrack;
import io.sfrei.tracksearch.tracks.Track;
import io.sfrei.tracksearch.tracks.YouTubeTrack;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;
import plugway.mc.music.disc.dj.MusicDiskDj;
import plugway.mc.music.disc.dj.books.TextbookLogic;
import plugway.mc.music.disc.dj.config.ConfigurationManager;
import plugway.mc.music.disc.dj.files.FileManager;
import plugway.mc.music.disc.dj.gui.handlers.ProgressBarHandler;
import plugway.mc.music.disc.dj.gui.handlers.Status;
import plugway.mc.music.disc.dj.gui.handlers.StatusHandler;
import plugway.mc.music.disc.dj.gui.handlers.StatusLabelHandler;
import plugway.mc.music.disc.dj.gui.widgets.WClickablePlainPanel;
import plugway.mc.music.disc.dj.image.PreviewProvider;
import plugway.mc.music.disc.dj.image.TextureCreator;
import plugway.mc.music.disc.dj.music.converter.MusicConverter;
import plugway.mc.music.disc.dj.music.disks.Disk;
import plugway.mc.music.disc.dj.music.disks.MinecraftDiskProvider;
import plugway.mc.music.disc.dj.music.downloader.MusicDownloader;
import plugway.mc.music.disc.dj.resourcepacks.ResourcePackHandler;
import plugway.mc.music.disc.dj.search.LinkValidator;
import plugway.mc.music.disc.dj.search.MusicSearchProvider;
import plugway.mc.music.disc.dj.search.YouTubeMetadata;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainGui extends LightweightGuiDescription {
    private final int resultsCount = 20;
    private int chosenResult = -1;
    private boolean busy = false;
    private Queue<Runnable> runQueue = new LinkedList<>();
    private WTextField searchField = new WTextField(Text.translatable("musicdiskdj.name.field.suggestion"));
    //buttons
    private WButton searchButton = new WButton(Text.translatable("gui.recipebook.search_hint"));
    private WButton removeButton = new WButton(toText("-"));
    private WButton addButton = new WButton(toText("+"));
    private WButton makeCoolButton = new WButton(Text.translatable("structure_block.mode.save"));
    private WButton reconnectButton = new WButton(toText("⟳"));
    private WLabel soundCloudAvaiLable = new WLabel(toText("SoundCloud"));
    private WLabel youTubeAvaiLable = new WLabel(toText("YouTube"));
    private WClickablePlainPanel[] results = new WClickablePlainPanel[resultsCount];
    private WSprite[] preview = new WSprite[resultsCount];
    private WLabel[] name = new WLabel[resultsCount];
    private WLabel[] views = new WLabel[resultsCount];
    private WLabel[] duration = new WLabel[resultsCount];
    private WLabel[] channel = new WLabel[resultsCount];
    private WLabel[] sourceName = new WLabel[resultsCount];
    private List<Track> latestTracks = MusicSearchProvider.getEmptyList(resultsCount);

    private final int musicDisksCount = MinecraftDiskProvider.musicDisksCount;
    private int chosenMusicDisk = -1;
    private WClickablePlainPanel[] disks = new WClickablePlainPanel[musicDisksCount];
    private WSprite[] disksPreview = new WSprite[musicDisksCount];
    private WLabel[] diskName = new WLabel[musicDisksCount];
    private WLabel[] diskAuthor = new WLabel[musicDisksCount];
    private List<Track> musicDisks = MusicSearchProvider.getEmptyList(musicDisksCount);
    private Identifier blankTexture = new Identifier("mcmddj", "textures/blank_debug1.png");
    private StatusHandler statusHandler = new StatusHandler(new ProgressBarHandler(WBar.Direction.RIGHT, 0, 100),
            new StatusLabelHandler());


    private enum inTheAreaOf {
        results, disks
    }

    public MainGui() {
        busy = true;

        WPlainPanel root = new WPlainPanel();
        root.setSize(640,360);
        setRootPanel(root);


        searchField.setEditable(true);
        searchField.setMaxLength(50);
        root.add(searchField, 16, 16, 331, 1);

        searchButton.setOnClick(performSearch());
        root.add(searchButton, searchField.getX()+searchField.getWidth() + 10,searchField.getY(), 65, 20);


        //RESULTS BLOCK
        WPlainPanel resultLabelPanel = new WPlainPanel();
        resultLabelPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        resultLabelPanel.setSize(searchField.getWidth(), 22);
        WLabel resultLabel = new WLabel(Text.translatable("musicdiskdj.name.label.result"));
        resultLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        reconnectButton.setOnClick(reconnect());
        WLabel commaLabel = new WLabel(toText(", "));
        soundCloudAvaiLable.setVerticalAlignment(VerticalAlignment.CENTER);
        soundCloudAvaiLable.setColor(0xFFFFFF);
        soundCloudAvaiLable.setSize(56, resultLabelPanel.getHeight()-2);
        commaLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        commaLabel.setSize(6, resultLabelPanel.getHeight()-2);
        youTubeAvaiLable.setVerticalAlignment(VerticalAlignment.CENTER);
        youTubeAvaiLable.setColor(0xFFFFFF);
        youTubeAvaiLable.setSize(42, resultLabelPanel.getHeight()-2);
        resultLabelPanel.add(resultLabel, 4, 2, resultLabelPanel.getWidth()-8, resultLabelPanel.getHeight()-2);
        resultLabelPanel.add(reconnectButton, resultLabelPanel.getWidth()-20, 1, 20, 20);
        resultLabelPanel.add(soundCloudAvaiLable, reconnectButton.getX()-soundCloudAvaiLable.getWidth()-4, 2, soundCloudAvaiLable.getWidth(), resultLabelPanel.getHeight()-2);
        resultLabelPanel.add(commaLabel, soundCloudAvaiLable.getX()-commaLabel.getWidth(), 2, commaLabel.getWidth(), resultLabelPanel.getHeight()-2);
        resultLabelPanel.add(youTubeAvaiLable, commaLabel.getX()-youTubeAvaiLable.getWidth(), 2, youTubeAvaiLable.getWidth(), resultLabelPanel.getHeight()-2);
        root.add(resultLabelPanel, searchField.getX(), searchField.getY()+searchField.getHeight()-2, resultLabelPanel.getWidth(), resultLabelPanel.getHeight());

        WPlainPanel resultPanel = new WPlainPanel();
        resultPanel.setSize(405, 200);
        WScrollPanel resultScrollPanel = new WScrollPanel(resultPanel);
        resultScrollPanel.setScrollingHorizontally(TriState.FALSE);
        resultScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(resultScrollPanel, resultLabelPanel.getX(), resultLabelPanel.getY()+resultLabelPanel.getHeight()-1, 405, 258);

        for (int i = 0; i < resultsCount; i++){
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}
            //var isEmptyTrack = MusicSearchProvider.isEmptyTrack(track);

            results[i] = new WClickablePlainPanel();
            results[i].setAllowToClick(false);
            results[i].setOnClick(choose(i, results, inTheAreaOf.results)).onClick(0, 0, GLFW.GLFW_MOUSE_BUTTON_1);
            preview[i] = new WSprite(blankTexture);
            name[i] = new WLabel(toText(""));
            views[i] = new WLabel(toText(""));
            duration[i] = new WLabel(toText(""));
            channel[i] = new WLabel(toText(""));
            sourceName[i] = new WLabel(toText(""));
            sourceName[i].setHorizontalAlignment(HorizontalAlignment.RIGHT);

            results[i].add(preview[i], 3, 3, 90, 50);
            results[i].add(name[i], preview[i].getX()+preview[i].getWidth()+10, preview[i].getY()+3);
            results[i].add(channel[i], preview[i].getX()+preview[i].getWidth()+10, name[i].getY()+10);
            results[i].add(views[i], preview[i].getX()+preview[i].getWidth()+10, channel[i].getY()+10);
            results[i].add(duration[i], preview[i].getX()+preview[i].getWidth()+10, views[i].getY()+10);
            results[i].add(sourceName[i], preview[i].getX()+preview[i].getWidth()+10, duration[i].getY(), 282, 0);

            resultPanel.add(results[i], 5, 5+i*60, 388, 56);//386 60
        }
        resultPanel.setSize(405, 6+resultsCount*60);//move to update results for dynamic results count
        updateResults();


        //MUSIC DISKS BLOCK
        WPlainPanel disksPanel = new WPlainPanel();
        disksPanel.setSize(120, 535);
        WScrollPanel disksScrollPanel = new WScrollPanel(disksPanel);
        disksScrollPanel.setScrollingHorizontally(TriState.FALSE);
        disksScrollPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        root.add(disksScrollPanel, root.getWidth() - 190 - 15, 15, 190, root.getHeight()-30);

        for (int i = 0; i < musicDisksCount; i++){
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks.get(i);
            disks[i] = new WClickablePlainPanel();
            disks[i].setAllowToClick(false);
            disks[i].setOnClick(choose(i, disks, inTheAreaOf.disks));
            disks[i].setAllowToClick(true);
            disksPreview[i] = new WSprite(disk.getId());
            if (MusicSearchProvider.isEmptyTrack(track)){
                diskAuthor[i] = new WLabel(toText(disk.getAuthor()));
                diskName[i] = new WLabel(toText(disk.getName()));
            } else {
                diskAuthor[i] = new WLabel(toText(track.getTrackMetadata().getChannelName()));
                diskName[i] = new WLabel(cutStringTo(20, track.getTitle()));
            }
            diskAuthor[i].setVerticalAlignment(VerticalAlignment.CENTER);
            diskName[i].setVerticalAlignment(VerticalAlignment.CENTER);

            disks[i].add(disksPreview[i], 3, 0, 35, 35);
            disks[i].add(diskAuthor[i], disksPreview[i].getX()+disksPreview[i].getWidth()+3, disksPreview[i].getY()+4);
            disks[i].add(diskName[i], diskAuthor[i].getX(), diskAuthor[i].getY()+diskAuthor[i].getHeight()-8);

            disksPanel.add(disks[i], 5, 5+i*39, 173, 0);
        }
        var disksSpacer = new WPlainPanel();
        disksSpacer.setSize(0, 0);
        disksPanel.add(disksSpacer, 5, musicDisksCount*40, 0, 5); //space after last disk in list 4pixels

        //turning on outline / possibly remove?
        choose(chosenResult, results, inTheAreaOf.results).run();
        choose(chosenMusicDisk, disks, inTheAreaOf.disks).run();


        //operation buttons and bottom left part

        removeButton.setOnClick(removeTrackFromDisks(removeButton));
        root.add(removeButton, resultScrollPanel.getX()+resultScrollPanel.getWidth()-20, disksScrollPanel.getY()+disksScrollPanel.getHeight()-20, 20, 20);

        addButton.setOnClick(addTrackToDisks(addButton));
        root.add(addButton, removeButton.getX()-25, removeButton.getY(), 20, 20);

        makeCoolButton.setOnClick(createResourcePack(makeCoolButton, addButton, removeButton, searchButton));
        root.add(makeCoolButton, addButton.getX() - 55, removeButton.getY(), 50, 20);

        root.add(statusHandler.getProgressBar(), resultScrollPanel.getX(), removeButton.getY(), 300, 6);
        statusHandler.getStatusLabel().setVerticalAlignment(VerticalAlignment.BOTTOM).setHorizontalAlignment(HorizontalAlignment.LEFT);
        root.add(statusHandler.getStatusLabel(), statusHandler.getProgressBar().getX(), removeButton.getY(), statusHandler.getProgressBar().getWidth(), removeButton.getHeight());


        root.validate(this);

        //animation
        new Thread(this::animateSearchFieldText).start();
        //connect to services
        busy = false;
        addToQueue(reconnect());
        tryToRunNextTask();
    }

    private void animateSearchFieldText() {
        int suggestionColor = 0x808080;
        int minColor = 0x0A0A0A;
        int maxColor = 0xF0F0F0;
        int shiftValue = 10;
        int currentColor = suggestionColor;
        int sleepTime = 50;
        try {
            for (int i = 0; i < 3; i++) {
                while (currentColor > minColor){//fade out suggestion
                    currentColor = shiftColor(-shiftValue, currentColor);
                    searchField.setSuggestionColor(currentColor);
                    Thread.sleep(sleepTime);
                }
                searchField.setSuggestion(Text.translatable("musicdiskdj.name.mcmddj"));
                while (currentColor < maxColor){//fade in mcmddj
                    currentColor = shiftColor(shiftValue, currentColor);
                    searchField.setSuggestionColor(currentColor);
                    Thread.sleep(sleepTime);
                }
                while (currentColor > minColor){//fade out mcmddj
                    currentColor = shiftColor(-shiftValue, currentColor);
                    searchField.setSuggestionColor(currentColor);
                    Thread.sleep(sleepTime);
                }
                searchField.setSuggestion(Text.translatable("musicdiskdj.name.field.suggestion"));
                while (currentColor < suggestionColor){//fade in suggestion
                    currentColor = shiftColor(shiftValue, currentColor);
                    searchField.setSuggestionColor(currentColor);
                    Thread.sleep(sleepTime);
                }
            }
        } catch (Exception e){
            searchField.setSuggestionColor(suggestionColor);
            searchField.setSuggestion(Text.translatable("musicdiskdj.name.field.suggestion"));
        }
    }
    private static int shiftColor(int value, int color){//move to color utils
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red += value;
        green += value;
        blue += value;

        return (red << 16) | (green << 8) | blue;
    }

    public Runnable choose(int index, WClickablePlainPanel[] area, inTheAreaOf areaOf){
        return () -> {
            if (index < 0 || index >= area.length){
                if (areaOf == inTheAreaOf.results)
                    chosenResult = -1;
                else
                    chosenMusicDisk = -1;
            }
            for (int i = 0; i < area.length; i++){          //maybe remove?
                if(i == index){
                    area[i].setBackgroundPainter(BackgroundPainter.createColorful(0));
                    if (areaOf == inTheAreaOf.results)
                        chosenResult = i;
                    else
                        chosenMusicDisk = i;
                }
                else
                    area[i].setBackgroundPainter(null);
            }
        };
    }
    public Runnable performSearch(){
        return () -> {
            setBusy();
            Thread searchThread = new Thread(() -> {
                String query = searchField.getText();
                if (query.equals(""))
                    return;

                statusHandler.getProgressBarHandler().setSectionsCount(2);
                statusHandler.setStatus(1, Status.searching);//status
                latestTracks = MusicSearchProvider.musicSearch(query);

                updateResults();

                setNotBusy();
            });
            searchThread.start();
        };
    }
    private void updateResults(){
        statusHandler.setStatus(Status.updatingResults);
        statusHandler.getProgressBarHandler().nextSection();
        for (int i = 0; i < resultsCount; i++){
            statusHandler.setStatus((i+1d)/resultsCount);
            Track track = MusicSearchProvider.getEmptyTrack();
            try {
                track = latestTracks.get(i);
            } catch (Exception ignored){}
            var isEmptyTrack = MusicSearchProvider.isEmptyTrack(track);
            name[i].setText(cutStringTo(42, track.getCleanTitle()));
            if (!isEmptyTrack){
                duration[i].setText(toText(track.durationFormatted()));
                var ident = PreviewProvider.getIdentifier(track.getTrackMetadata().getThumbNailUrl(), "result_" + i);
                preview[i].setImage(ident);
                channel[i].setText(toText(track.getTrackMetadata().getChannelName()));
                if(track.getSource() == TrackSource.Youtube){
                    views[i].setText(toPrettyString(track.getTrackMetadata().getStreamAmount(), " views"));
                    sourceName[i].setText(toText("YouTube"));
                    sourceName[i].setColor(0xc90000);
                } else {
                    views[i].setText(toPrettyString(track.getTrackMetadata().getStreamAmount(), " plays"));
                    sourceName[i].setText(toText("SoundCloud"));
                    sourceName[i].setColor(0xc94200);
                }

                results[i].setAllowToClick(true);
            } else {
                views[i].setText(toText(""));
                duration[i].setText(toText(""));
                sourceName[i].setText(toText(""));
                preview[i].setImage(blankTexture);
                channel[i].setText(toText(""));
                results[i].setAllowToClick(false);
            }
            results[i].setBackgroundPainter(null);
            chosenResult = -1;
        }
        if(statusHandler.getProgressBarHandler().isLastSection())
            statusHandler.reset();
    }
    private void updateDisks(){
        for (int i = 0; i < musicDisksCount; i++) {
            Track track = musicDisks.get(i);
            Disk disk = MinecraftDiskProvider.disks.get(i);
            if (MusicSearchProvider.isEmptyTrack(track)) {
                diskAuthor[i].setText(toText(disk.getAuthor()));
                diskName[i].setText(toText(disk.getName()));
            } else {
                diskAuthor[i].setText(toText(getTrueTitle(track.getCleanTitle(), track.getTrackMetadata().getChannelName()).split(" - ")[0]));
                diskName[i].setText(cutStringTo(20, getTrueTitle(track.getCleanTitle(), track.getTrackMetadata().getChannelName()).split(" - ")[1]));
            }
        }
    }
    private Runnable reconnect(){
        return () -> {
            setBusy();
            Thread connectThread = new Thread(() -> {

                MusicSearchProvider.connect(this, statusHandler);
                setNotBusy();
            });
            connectThread.start();
        };
    }
    private Runnable addTrackToDisks(WButton addButton){
        return () -> {
            if (chosenMusicDisk == -1 || chosenResult == -1)
                return;
            addButton.setEnabled(false);
            musicDisks.set(chosenMusicDisk, latestTracks.get(chosenResult));
            updateDisks();
            addButton.setEnabled(true);
        };
    }
    private Runnable removeTrackFromDisks(WButton removeButton){
        return () -> {
            if (chosenMusicDisk == -1)
                return;
            removeButton.setEnabled(false);
            musicDisks.set(chosenMusicDisk, MusicSearchProvider.getEmptyTrack());
            updateDisks();
            removeButton.setEnabled(true);
        };
    }
    public Runnable completeExport(){
        return () -> {
            setBusy();
            Thread exportThread = new Thread(() -> {
                statusHandler.getProgressBarHandler().setSectionsCount(2);
                statusHandler.setStatus(Status.epGettingTracks);
                var exported = TextbookLogic.getExported();
                TextbookLogic.clearState();
                var exportedTracks = YouTubeMetadata.getTracks(exported, true, statusHandler);
                statusHandler.getProgressBarHandler().nextSection();
                statusHandler.setStatus(Status.epAddingTracks);
                for (int i = 0; i < musicDisks.size(); i++) {
                    if (i >= exportedTracks.size()){
                        musicDisks.set(i, MusicSearchProvider.getEmptyTrack());
                        continue;
                    }
                    musicDisks.set(i, exportedTracks.get(i));
                }
                updateDisks();
                statusHandler.reset();
                setNotBusy();
            });
            exportThread.start();
        };
    }
    private Runnable createResourcePack(WButton makeCoolButton, WButton addButton, WButton removeButton, WButton searchButton){
        return () -> {
            setBusy();
            Thread creationThread = new Thread(() -> {
                try {
                    ConfigurationManager.clear();
                    statusHandler.getProgressBarHandler().setSectionsCount(3);
                    statusHandler.setStatus(Status.cpCopyFiles);
                    FileUtils.copyDirectory(new File(MusicDiskDj.templatePath), new File(MusicDiskDj.resultPath));
                    File langFile = new File(MusicDiskDj.resultPath+"\\assets\\minecraft\\lang\\en_us.json");
                    String langFileContent = FileUtils.readFileToString(langFile, StandardCharsets.UTF_8);
                    //downloading files
                    statusHandler.getProgressBarHandler().nextSection();
                    for (int i = 0; i < musicDisksCount; i++){
                        statusHandler.setStatus((i+1d)/musicDisksCount);
                        Track track = musicDisks.get(i);
                        if (MusicSearchProvider.isEmptyTrack(track))
                            continue;

                        //dealing with music
                        statusHandler.setStatus(Status.cpDownloadingMusic);
                        File oggTrackFile = MusicConverter.downloadOgg(track, i);
                        //File oggTrackFile = new File(MusicDiskDj.tempPath+"\\"+trackFile.getName().split("\\.")[0]+".ogg");
                        //converter.mp3ToOgg(trackFile, oggTrackFile);
                        FileUtils.copyFile(oggTrackFile, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\sounds\\records\\"+ MinecraftDiskProvider.disks.get(i).getName().toLowerCase()+".ogg"));

                        //dealing with textures
                        statusHandler.setStatus(Status.cpCreatingTexture);
                        File previewFile = MusicDownloader.downloadPreview(track, i);
                        Image preview = ImageIO.read(previewFile);
                        TextureCreator.modifyTexture(preview, new File(MusicDiskDj.resultPath+
                                "\\assets\\minecraft\\"+ MinecraftDiskProvider.disks.get(i).getId().toString().split(":")[1]));

                        //dealing with names
                        langFileContent = langFileContent.replaceAll(MinecraftDiskProvider.disks.get(i).getAuthor()+" - "+
                                MinecraftDiskProvider.disks.get(i).getName(), getTrueTitle(track.getCleanTitle(), track.getTrackMetadata().getChannelName()));

                        //adding to config
                        if (track instanceof YouTubeTrack)
                            ConfigurationManager.set(LinkValidator.getYTId(track.getUrl()), i);
                        if (track instanceof SoundCloudTrack)
                            System.out.println("SC track config");//implementation needed
                    }
                    statusHandler.getProgressBarHandler().nextSection();
                    FileUtils.write(langFile, langFileContent, Charset.defaultCharset());

                    //adding to archive
                    statusHandler.setStatus(1.0/4, Status.cpCreatingArchive);
                    File outputArchive = new File(MusicDiskDj.tempPath+"\\mcmddj_result.zip");
                    FileManager.archiveDirContents(new File(MusicDiskDj.resultPath), outputArchive);

                    //copying archive to resource packs and reenabling it
                    statusHandler.setStatus(1.0/4*2);
                    ResourcePackHandler.DisableResourcePack("file/mcmddj_result.zip");
                    FileUtils.copyFile(outputArchive, new File(MusicDiskDj.mcDirectoryPath+"\\resourcepacks\\mcmddj_result.zip"));
                    ResourcePackHandler.EnableResourcePack("file/mcmddj_result.zip");

                    //saving config and updating disks
                    ConfigurationManager.saveConfig();
                    updateDisks();

                    //collecting garbage
                    statusHandler.setStatus(1.0/4*3);
                    FileUtils.cleanDirectory(new File(MusicDiskDj.tempPath));
                    new File(MusicDiskDj.resultPath).mkdir();

                    statusHandler.reset();
                    setNotBusy();
                } catch (Exception e){e.printStackTrace();}

            });
            creationThread.start();
        };
    }
    private Text cutStringTo(int charNum, String string){//move somewhere else
        if (string.length() > charNum)
            string = string.substring(0,charNum) + "...";
        return toText(string);
    }
    private Text toPrettyString(Long number, String endsWith){//move somewhere else
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###", symbols);
        return toText(formatter.format(number) + endsWith);
    }
    private Text toText(String string){
        return Text.of(string);
    }//move somewhere else
    private void setBusy(){
        busy = true;

        TextbookLogic.disableAllButtons();

        makeCoolButton.setEnabled(false);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        searchButton.setEnabled(false);
        reconnectButton.setEnabled(false);
    }
    private void setNotBusy(){
        busy = false;

        if (tryToRunNextTask())
            return;

        TextbookLogic.enableAllButtons();

        makeCoolButton.setEnabled(true);
        addButton.setEnabled(true);
        removeButton.setEnabled(true);
        searchButton.setEnabled(true);
        reconnectButton.setEnabled(true);
    }
    public void colorYTConnected(){
        youTubeAvaiLable.setColor(0xc90000);
    }//move somewhere else
    public void colorYTFailedToConnect(){
        youTubeAvaiLable.setColor(0xffffff);
    }//move somewhere else
    public void colorSCConnected(){
        soundCloudAvaiLable.setColor(0xc94200);
    }//move somewhere else
    public void colorSCFailedToConnect(){
        soundCloudAvaiLable.setColor(0xffffff);
    }//move somewhere else

    private String getTrueTitle(String title, String channelName){//move somewhere else
        if (title.contains(" - "))
            return title;
        if (title.contains("-"))
            return title.replaceFirst("-", " - ");
        return  channelName + " - " + title;
    }

    public boolean tryToRunNextTask(){
        if (!busy && runQueue.size() != 0){
            runQueue.poll().run();
            return true;
        }
        return false;
    }
    public void addToQueue(Runnable task){
        runQueue.add(task);
    }
}
