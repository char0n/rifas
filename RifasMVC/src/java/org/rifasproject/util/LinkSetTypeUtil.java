/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.rifasproject.domain.DictionaryItem;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.LinkSetTypeSearchBarItem;

/**
 *
 * @author char0n
 */
public class LinkSetTypeUtil {

    private static final Map<String, Set<String>> types                = new HashMap<String, Set<String>>();
    private static final List<LinkSetTypeSearchBarItem> searchBarItems = new ArrayList<LinkSetTypeSearchBarItem>();
    private static final Map<String, LinkSetTypeSearchBarItem> cache   = new HashMap<String, LinkSetTypeSearchBarItem>();

    static {
        // Common identifying data
        DictionarySet barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "all"));
        barItemProperties.add(new DictionaryItem("text", "All Files"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.UNSPECIFIED.name()));
        LinkSetTypeSearchBarItem barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "archives"));
        barItemProperties.add(new DictionaryItem("text", "Archives"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.ARCHIVES.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "video"));
        barItemProperties.add(new DictionaryItem("text", "Video"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.VIDEO.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "audio"));
        barItemProperties.add(new DictionaryItem("text", "Audio"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.AUDIO.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "docs"));
        barItemProperties.add(new DictionaryItem("text", "Docs"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.DOCS.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "pictures"));
        barItemProperties.add(new DictionaryItem("text", "Pictures"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.PICTURES.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "application"));
        barItemProperties.add(new DictionaryItem("text", "Applications"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.APPLICATIONS.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "games"));
        barItemProperties.add(new DictionaryItem("text", "Games"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.GAMES.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        barItemProperties = new DictionarySet();
        barItemProperties.add(new DictionaryItem("acronym", "cddvd"));
        barItemProperties.add(new DictionaryItem("text", "CD/DVD"));
        barItemProperties.add(new DictionaryItem("type", LinkSetType.CDDVD.name()));
        barItem = new LinkSetTypeSearchBarItem();
        barItem.setDictionary(barItemProperties);
        searchBarItems.add(barItem);

        // Common extensions
        types.put(LinkSetType.UNSPECIFIED.name(), new HashSet<String>());
        types.put(LinkSetType.ARCHIVES.name(), new HashSet<String>(Arrays.asList(new String[] {".a", ".ar", ".cpio", ".shar", ".tar", ".bz2", ".F", ".gz", ".lzma",
                                                                                         ".lzo", ".rz", ".z", ".Z", ".7z", ".ace", ".arc", ".alz", ".arj", ".cab",
                                                                                         ".cpt", ".dar", ".dgc", ".dmg", ".gca", ".ice", ".j", ".jar", ".lzh", ".lza",
                                                                                         ".lzx", ".partimage", ".pq6", ".qda", ".rar", ".rk", ".sea", ".sit", ".sqk",
                                                                                         ".tgz", ".tbz2", ".uha", ".zip", ".zoo", ".zz"})));
        types.put(LinkSetType.AUDIO.name(), new HashSet<String>(Arrays.asList(new String[] {".mp3", ".mp2", ".mp1", ".wma", ".mid", ".midi", ".rmi",
                                                                                     ".kar", ".miz", ".cda", ".wav", ".voc", ".au", ".snd", ".aif", ".aiff", ".ogg", ".nsa"})));

        types.put(LinkSetType.VIDEO.name(), new HashSet<String>(Arrays.asList(new String[] {".mpg", ".mpeg", ".mp2", ".m1v", ".m2v", ".avi", ".asf", ".avi", ".vmf", ".mov", ".mp4", ".wmv"})));
        types.put(LinkSetType.DOCS.name(), new HashSet<String>(Arrays.asList(new String[] {".doc", ".txt", ".rtf", ".pdf", ".djvu", ".odt", ".ott", ".sxw", ".stw", ".sdw", ".vor", ".html", ".htm", ".pdb", ".xm", ".psw"})));
        types.put(LinkSetType.PICTURES.name(), new HashSet<String>(Arrays.asList(new String[] {".iff", ".dwg", ".dxf", ".afx", ".brk", ".301", ".cal", ".cals", ".gif", ".cgm", ".cmx",
                                                                                        ".cdr", ".lbm", ".cut", ".eps", ".ai", ".ps", ".fpx", ".img", ".hgl", ".hpg", ".pgl", ".jpg",
                                                                                        ".jif", ".jpeg", ".jp2", ".j2c", ".jpc", ".jpx", ".kdc", ".pcd", ".kfx", ".lv", ".pic", ".pct",
                                                                                        ".mac", ".drw", ".msp", ".dgn", ".ncr", ".pspimage", ".psp", ".psd", ".pbm", ".pgm",
                                                                                        ".png", ".ppm", ".raw", ".svg", ".svgz", ".sct", ".ct", ".rgb", ".bw", ".rgba", ".sgi", ".ras",
                                                                                        ".tif", ".tiff", ".tga", ".gem", ".clp", ".cur", ".rle", ".bmp", ".dib", ".wbmp",
                                                                                        ".wbm", ".wpg", ".xbm", ".bm", ".xwd", ".wd", ".xpm", ".dcx", ".pcx"})));
        types.put(LinkSetType.APPLICATIONS.name(), new HashSet<String>(Arrays.asList(new String[] {".exe", ".com", ".bat", ".sh", ".class", ".msi"})));
        types.put(LinkSetType.GAMES.name(), new HashSet<String>());
        types.put(LinkSetType.CDDVD.name(), new HashSet<String>(Arrays.asList(new String[] {".iso", ".img", ".mds", ".mdf", ".bwt", ".ccd", ".cdi", ".pdi", ".nrg", ".cue"})));

        // Incorporating extensions as simple comma delimited string into dictionary
        String exts = "";
        for (LinkSetTypeSearchBarItem item : searchBarItems) {
            String type = item.getDictionary().findByKey("type").getValue();
            for (String ext : types.get(type)) {
                exts += ext+", ";
            }
            exts = (!exts.equals("")) ? exts.substring(0, exts.length() - 2) : exts;
            item.getDictionary().add(new DictionaryItem("extensions", exts));
        }
    }

    public static List<LinkSetTypeSearchBarItem> getSearchBarItems() {
        return searchBarItems;
    }

    public static LinkSetTypeSearchBarItem searchItemByAcronym(String acronym) {
        //Quick load from cache
        if (cache.containsKey("acronym-"+acronym)) {
            return cache.get("acronym-"+acronym);
        }

        LinkSetTypeSearchBarItem item = null;
        // Slow loop search
        for (LinkSetTypeSearchBarItem i : searchBarItems) {
            if (i.getDictionary().findByKey("acronym").getValue().equals(acronym)) {
                item = i;
                cache.put("acronym-"+acronym, item);
                break;
            }
        }
        return item;
    }

    public static LinkSetType searchLinkSetTypeByAcronym(String acronym) {
        LinkSetTypeSearchBarItem item = searchItemByAcronym(acronym);
        if (item == null) {
            return LinkSetType.UNSPECIFIED;
        }
        String type = item.getDictionary().findByKey("type").getValue();
        return LinkSetType.valueOf(type);
    }

    public static String searchTextByAcronym(String acronym) {
        LinkSetTypeSearchBarItem item = searchItemByAcronym(acronym);
        if (item == null) {
            return "";
        }
        String text = item.getDictionary().findByKey("text").getValue();
        return text;
    }

    public static LinkSetTypeSearchBarItem searchItemByLinkSetType(LinkSetType type) {
        //Quick load from cache
        if (cache.containsKey("linkSetType-"+type.name())) {
            return cache.get("linkSetType-"+type.name());
        }

        LinkSetTypeSearchBarItem item = null;
        // Slow loop search
        for (LinkSetTypeSearchBarItem i : searchBarItems) {
            if (i.getDictionary().findByKey("type").getValue().equals(type.name())) {
                item = i;
                cache.put("linkSetType-"+type.name(), item);
                break;
            }
        }
        return item;
    }

    public static String searchAcronymByLinkSetType(LinkSetType type) {
        LinkSetTypeSearchBarItem item = searchItemByLinkSetType(type);
        if (item == null) {
            return "all";
        }
        String acronym = item.getDictionary().findByKey("acronym").getValue();
        return acronym;
    }

    public static String searchTextByLinkSetType(LinkSetType type) {
        LinkSetTypeSearchBarItem item = searchItemByLinkSetType(type);
        if (item == null) {
            return "All Files";
        }
        String text = item.getDictionary().findByKey("text").getValue();
        return text;
    }

    public static LinkSetType getTypeByDotExtension(String ext) {
        for (String type : types.keySet()) {
            Set<String> exts = types.get(type);
            if (exts.contains(ext)) {
                return LinkSetType.valueOf(type);
            }
        }
        return LinkSetType.UNSPECIFIED;
    }

    public static LinkSetType getTypeByExtension(String ext) {
        ext = "."+ext;
        for (String type : types.keySet()) {
            Set<String> exts = types.get(type);
            if (exts.contains(ext)) {
                return LinkSetType.valueOf(type);
            }
        }
        return LinkSetType.UNSPECIFIED;
    }

    public static LinkSetType getTypeByFileName(String fileName) {

        fileName         = fileName.replaceFirst("\\.[0-9]+$", "");
        LinkSetType type = LinkSetType.UNSPECIFIED;
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex != -1) {
            type = getTypeByDotExtension(fileName.substring(dotIndex));
        }

        return type;
    }
}