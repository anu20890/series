package com.series;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static Map<String, Integer> getSeasonEpisodeNoFromVideo(String fileName) {
		Map<String, Integer> detail = new HashMap<String, Integer>();
		Pattern pattern = Pattern.compile(".+\\.[S|s](\\d\\d[e|E]\\d\\d)\\..+");
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			String[] info = matcher.group(1).split("[e|E]");
			detail.put("season", Integer.valueOf(Integer.parseInt(info[0])));
			detail.put("episode", Integer.valueOf(Integer.parseInt(info[1])));
		}
		return detail;
	}

	public static Map<String, Integer> getSeasonEpisodeNoFromSubtitles(String fileName) {
		Map<String, Integer> detail = new HashMap<String, Integer>();
		Pattern pattern = Pattern.compile(".+ - (\\d\\d[x|X]\\d\\d) - .+");
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			String[] info = matcher.group(1).split("[x|X]");
			detail.put("season", Integer.valueOf(Integer.parseInt(info[0])));
			detail.put("episode", Integer.valueOf(Integer.parseInt(info[1])));
		}
		return detail;
	}

	public static File getAssociatedSrtFile(List<File> srtFiles, File videoFile) {
		return srtFiles.stream().filter(srtFile -> {
			Map<String, Integer> vidMap = Utils.getSeasonEpisodeNoFromVideo(videoFile.getName());
			Map<String, Integer> srtMap = Utils.getSeasonEpisodeNoFromSubtitles(srtFile.getName());
			return (vidMap.get("season") == srtMap.get("season")) && (vidMap.get("episode") == srtMap.get("episode"));
		}).findFirst().get();
	}
}
