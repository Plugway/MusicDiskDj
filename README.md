![](media/logo.png "Poggers")
# Music Disk DJ

---
This mod allows you to seamlessly search music from YouTube and Soundcloud within the game and provides the functionality to replace vanilla music discs with your favorite songs. Also share your playlists with other players via books (beta 0.8.0, YT only for now). The mod is designed to be fully client-side.

## Download

---
You can find a ready-made build of this mod on the [releases page](https://github.com/Plugway/MusicDiskDj/releases).

## Dev Section

---
### How to Build this "Amazing" Project
1. First, clone the repository for yourself.
2. Open it as a project in your IDE. Don't worry, it'll probably take forever to load. Just grab a cup of coffee and contemplate your life choices.
3. Now comes the fun part: try to build the project! But guess what? Nothing works! Surprise, surprise! Enjoy deciphering those cryptic error messages for the next 6 hours. Good luck!
4. After countless moments of frustration, confusion, and questioning your existence, you might start to understand why nothing works. Or maybe not. Who knows? It's all part of the adventure!
5. Spend another eternity re-reading guides, searching Stack Overflow, and banging your head against the keyboard. Just when you think you're close to giving up, try reopening the project. Miraculously, it might actually build this time. Or not. The suspense is killing you, isn't it?
6. Finally, after what feels like a lifetime of pain and suffering, you manage to build the project. Congratulations! You deserve a medal for surviving this ordeal. Now go celebrate with some ice cream or a stiff drink. You've earned it.

---
### TODO:  
- [x] <span style="background:#ff4d4f">add soundcloud support </span>
	- [x] sc/yt link label
	- [ ] parse sc links
	- [ ] import/export through books
	- [x] add reconnect button and connection info (Search results from: YT, SC    🔄)
- [ ] <span style="background:#d3f8b6">directional sound option</span> (low priority)
- [ ] <span style="background:#fff88f">dynamic results count</span> (low priority)
- [ ] <span style="background:#d3f8b6">Resource pack is not enabling when created for the first time</span> (low priority)
- [ ] Linux support
- [x] Dark mode support
- [x] <span style="background:#fff88f">remove garbage code  </span>
- [x] System.out.println -> logger
- [x] <span style="background:#d3f8b6">handle all ignored exceptions</span>
	- [x] <span style="background:#fff88f">Fix freezing when trying to download when yt-dlp is not allowed to access the Internet (try again after a while)</span>
- [x] <span style="background:#ff4d4f">Cache tracks with info for faster minor changes in playlist</span>
- [x] <span style="background:#affad1">add missing cutStringTo();</span>
- [x] <span style="background:#fff88f">Fix special characters in names when exporting</span>
- [x] <span style="background:#affad1">Fix names issue</span>
- [x] <span style="background:#ff4d4f">add import/export through books</span>
- [x] <span style="background:#d3f8b6">Disable interactions with results before update completes(fixed with different approach)</span>
- [x] <span style="background:#affad1">Highlight discs with tracks</span>
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