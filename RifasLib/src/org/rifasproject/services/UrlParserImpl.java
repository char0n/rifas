/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.Tag;
import org.rifasproject.util.RegexRepository;
import org.rifasproject.util.StringLengthComparator;

/**
 *
 * @author char0n
 */
public abstract class UrlParserImpl implements UrlParser {

    public static final Pattern pat1 = Pattern.compile("(.*)(\\.|_|-)part[0]*[1].rar$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat2 = Pattern.compile("(.*)(\\.|_|-)part[0-9]+.rar$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat3 = Pattern.compile("(.*)\\.rar$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat4 = Pattern.compile("(.*)\\.r\\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat5 = Pattern.compile("(.*)(\\.|_|-)\\d+$", Pattern.CASE_INSENSITIVE);

    public static final Pattern pat6 = Pattern.compile("(.*)\\.zip$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat7 = Pattern.compile("(.*)\\.z\\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat8 = Pattern.compile("(?is).*\\.7z\\.[\\d]+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat9 = Pattern.compile("(.*)\\.a.$", Pattern.CASE_INSENSITIVE);

    public static final Pattern pat10 = Pattern.compile("(.*)\\.[_a-z]{3}(\\.|$)");
    public static final Pattern pat11 = Pattern.compile("(.*)(\\.|_|-)[\\d]+($|\\.(otrkey|ac3|3gp|7zip|7z|aiff|aif|aifc|au|avi|bin|bz2|ccf|cue|deb|divx|dlc|doc|docx|dot|exe|ff|flv|gif|gz|iwd|iso|java|jpg|jpeg|mkv|mp2|mp3|mp4|mov|movie|mpe|mpeg|mpg|msi|msu|nfo|oga|ogg|ogv|pkg|png|pdf|ppt|pptx|pps|ppz|pot|psd|qt|rmvb|rar|r\\d+|\\d+|rpm|run|rsdf|rtf|sh|srt|snd|sfv|tar|tif|tiff|viv|vivo|wav|wmv|xla|xls|zip|z\\d+|ts|load|xpi|_[_a-z]{2})$)", Pattern.CASE_INSENSITIVE);

    public static final Pattern pat12 = Pattern.compile("(CD\\d+)", Pattern.CASE_INSENSITIVE);
    public static final Pattern pat13 = Pattern.compile("(part\\d+)", Pattern.CASE_INSENSITIVE);

    public static final Pattern pat14 = Pattern.compile("(.+)\\.+$");
    public static final Pattern pat15 = Pattern.compile("(.+)-+$");
    public static final Pattern pat16 = Pattern.compile("(.+)_+$");

    private RegexRepository linkRegex;
    private RegexRepository linkDescRegex;
    private Comparator<Link> comparator;

    @Override
    public void setLinkRegex(RegexRepository linkRegex) {
        this.linkRegex = linkRegex;
    }

    @Override
    public void setLinkDescRegex(RegexRepository linkDescRegex) {
        this.linkDescRegex = linkDescRegex;
    }

    @Override
    public void setComparator(Comparator<Link> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Comparator<Link> getComparator() {
        return this.comparator;
    }

    @Override
    public RegexRepository getLinkDescRegex() {
        return this.linkDescRegex;
    }

    @Override
    public RegexRepository getLinkRegex() {
        return this.linkRegex;
    }

    protected String getLinkSetName(String cleanFileName) {
        String linkSetName = null;

        linkSetName = cleanFileName.replace(".", " ").replace("_", " ").replace("-", " ").replaceAll("\\s+", " ").trim();

        return linkSetName;
    }

    protected Set<Tag> getDefaultTags(String linkSetName) {

          final int MIN_WORD_LENGTH = 3;
          final int MAX_WORD_LENGTH = 15;
          Pattern wordPattern       = Pattern.compile("(\\w)+");

          Matcher wordMatcher = wordPattern.matcher(linkSetName);
          List<String> wordsList = new ArrayList<String>();
          while (wordMatcher.find()) {
              String word = wordMatcher.group().toLowerCase();
              if (word.length() >= MIN_WORD_LENGTH && word.length() < MAX_WORD_LENGTH && !wordsList.contains(word)) {
                  wordsList.add(word);
              }
          }

          Collections.sort(wordsList, new StringLengthComparator());
          Collections.reverse(wordsList);

          Set<Tag> tags = new HashSet<Tag>();
          int i = 0;
          for (String word: wordsList) {
              // Take only four first longest tags
              if (i++ > 3) break;

              Tag tag = new Tag();
              tag.setBinder(word);
              tag.setCreated(new Date());
              tags.add(tag);
          }

          return tags;
    }

    protected String cleanFileName(String name) {
        /** remove rar extensions */
        name = getNameMatch(name, pat1);
        name = getNameMatch(name, pat2);
        name = getNameMatch(name, pat3);
        name = getNameMatch(name, pat4);
        name = getNameMatch(name, pat5);

        /**
         * remove 7zip/zip and hjmerge extensions
         */
        name = getNameMatch(name, pat6);
        name = getNameMatch(name, pat7);
        name = getNameMatch(name, pat8);
        name = getNameMatch(name, pat9);

        /**
         * FFSJ splitted files
         *
         * */
        name = getNameMatch(name, pat10);
        name = getNameMatch(name, pat11);

        /**
         * remove CDx,Partx
         */
        String tmpname = cutNameMatch(name, pat12);
        if (tmpname.length() > 3) name = tmpname;
        tmpname = cutNameMatch(name, pat13);
        if (tmpname.length() > 3) name = tmpname;

        /* remove extension */
        int lastPoint = name.lastIndexOf(".");
        if (lastPoint <= 0) lastPoint = name.lastIndexOf("_");
        if (lastPoint > 0) {
            if ((name.length() - lastPoint + 1) <= 3) {
                name = name.substring(0, lastPoint);
            }
        }
        /* remove ending ., - , _ */
        name = getNameMatch(name, pat14);
        name = getNameMatch(name, pat15);
        name = getNameMatch(name, pat16);
        return name;
    }

    private String getNameMatch(String name, Pattern pattern) {
        String match = null;

        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            match = matcher.group(1);
        } else {
            match = name;
        }

        return match;
    }

    private String cutNameMatch(String name, Pattern pattern) {
        if (name == null) return null;

        String match = null;
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            match = matcher.group(1);
        }
        if (match != null) {
            int firstpos = name.indexOf(match);
            String tmp = name.substring(0, firstpos);
            int lastpos = name.indexOf(match) + match.length();
            if (!(lastpos > name.length())) tmp = tmp + name.substring(lastpos);
            name = tmp;
            /* remove seq. dots */
            name = name.replaceAll("\\.{2,}", ".");
            name = name.replaceAll("\\.{2,}", ".");
        }
        return name;
    }
}