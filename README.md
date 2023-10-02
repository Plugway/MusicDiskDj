![](media/logo.png "Poggers")
# Music Disk DJ

---
This mod allows you to seamlessly search music from YouTube and Soundcloud within the game and provides the functionality to replace vanilla music discs with your favorite songs. The mod is designed to be fully client-side. In future updates, there will be a possibility to share your playlists with other players.

## Download

---
You can find a ready-made build of this mod on the [releases page](https://github.com/Plugway/MusicDiskDj/releases).

## Dev Section

---
TODO:  
- [ ] <span style="background:#ff4d4f">add import/export through books</span>
- [x] <span style="background:#ff4d4f">add soundcloud support </span>
	- [x] sc/yt link label
	- [ ] parse sc links
	- [x] add reconnect button and connection info (Search results from: YT, SC    🔄)
- [ ] <span style="background:#fff88f">remove garbage code  </span>
- [ ] <span style="background:#9254de">add global/local music switch</span>
- [ ] <span style="background:#fff88f">dynamic results count</span>
- [ ] <span style="background:#d3f8b6">handle all ignored exceptions</span>
- [ ] <span style="background:#fff88f">Fix freezing when trying to download when yt-dlp is not allowed to access the Internet (try again after a while)</span>
- [ ] <span style="background:#affad1">add missing cutStringTo();</span>
- [ ] <span style="background:#d3f8b6">Disable interactions with results before update completes</span>
- [x] <span style="background:#ff4d4f">Reset mod state when exiting to the main menu</span>
- [x] <span style="background:#d3f8b6">Disc names do not change after applying changes</span>
- [x] <span style="background:#40a9ff">read current downloaded songs from resource pack after minecraft reloading(from local config file? to share through books) </span>
- [x] Fix regex for yt links
- [x] <span style="background:#d2cbff">remove lag when open gui</span>
- [x] <span style="background:#d3f8b6">fix previews(how?)  </span>
	- [x] <span style="background:#fff88f">Remove generatePreviews thread call when gui opened first time</span>
- [x] <span style="background:#d3f8b6">add 1.20 disk</span>
- [x] <span style="background:#d3f8b6">disable search button when searching</span>
- [x] translate titles
- [x] <span style="background:#affad1">rework results block gui</span>
- [x] change pack format to 15 (1.20)
- [x] <span style="background:#d3f8b6">add statusbar  </span>
- [x] <span style="background:#d3f8b6">add progressbar </span>
- [x] <span style="background:#fff88f">split progress to sections somehow, switch sections in progress, each section from 0% to 100%</span>
- [x] <span style="background:#affad1">add name in main gui  (blink Music Disk DJ in search suggestion hint field)</span>
- [x] <span style="background:#ff4d4f">maybe add direct link paste</span>