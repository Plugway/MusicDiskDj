package plugway.mc.music.disc.dj.gui.handlers;

public enum Status {
    ready,
    searching,
    updatingResults,
    cpCopyFiles,        //creation process
    cpDownloadingMusic,
    cpCreatingTexture,
    cpCreatingArchive,
    connectingYT,
    connectingSC,
    epGettingTracks,    //export process
    epAddingTracks
}
