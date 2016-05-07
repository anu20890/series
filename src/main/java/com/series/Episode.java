package com.series;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class Episode {
	private File video_file;
	private File srt_file;

	public Episode(File video_file, File srt_file) {
		this.video_file = video_file;
		this.srt_file = srt_file;
	}

	private String getNewVidName() {
		return this.srt_file.getName().substring(0, this.srt_file.getName().indexOf("."))
				+ this.video_file.getName().split("[s|S]\\d\\d[e|E]\\d\\d")[1];
	}

	private String getNewSrtName() {
		String name = getNewVidName();
		return name.substring(0, name.length() - 3) + "srt";
	}

	public void rename() throws IOException {
		Path vid_path = this.video_file.toPath();
		Path srt_path = this.srt_file.toPath();
		String vid_name = getNewVidName();
		String srt_name = getNewSrtName();
		System.out.println("Renaming " + this.video_file.getName() + " To " + vid_name);
		Files.move(vid_path, vid_path.resolveSibling(vid_name), new CopyOption[0]);
		System.out.println("Renaming " + this.srt_file.getName() + " To " + srt_name);
		Files.move(srt_path, srt_path.resolveSibling(srt_name), new CopyOption[0]);
	}
}
