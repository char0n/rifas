/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.gui.task;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Movie;
import org.rifasproject.domain.MovieType;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.gui.RifasGUIApp;
import org.rifasproject.gui.RifasGUIView;
import org.rifasproject.services.LinkChecker;
import org.rifasproject.services.LinkCheckerException;
import org.rifasproject.services.ParsingException;
import org.rifasproject.services.SearchEngine;
import org.rifasproject.services.SearchEngineException;
import org.rifasproject.services.UrlDownloader;
import org.rifasproject.services.UrlParser;
import org.rifasproject.util.LinkSizeUtil;

/**
 *
 * @author char0n
 */
public class SearchLinksTask extends Task<List<LinkSet>, LinkSet> {

    private JTextField searchString;
    private Movie movie;
    private Tag tag;
    private JTree linksetTree;
    private int page;

    public SearchLinksTask(Application app, String movieName, MovieType movieType, String movieYear, JTextField searchString, JTree linksetTree, int page)
    {
        super(app);
        this.movie        = this.getMovieInstance(movieName, movieType, movieYear);
        this.tag          = new Tag();
        this.searchString = searchString;
        this.linksetTree  = linksetTree;
        this.page         = page;
        this.searchString.setText(movie.toString());
        setTitle("searchLinksTask");
        setDescription("task is searching for links in background");
        RifasGUIView mainView = (RifasGUIView) ((RifasGUIApp) app).getMainView();

        // Enable/Disable actions
        mainView.setSearchMoreEnabled(false);
        mainView.enableSearchCancel();
        
        // Clean tree
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.linksetTree.getModel().getRoot();
        if (this.page == 1) rootNode.removeAllChildren();
    }

    private Movie getMovieInstance(String movieName, MovieType movieType, String movieYear)
    {
        Movie movie = new Movie();
        movie.setName(movieName);
        movie.setType(movieType);
        movie.setYear(movieYear);

        return movie;
    }

    @Override
    protected List<LinkSet> doInBackground() {
    
        RifasGUIApp app           = (RifasGUIApp) getApplication();
        SearchEngine engine      = (SearchEngine) app.getBeanFactory().getBean("searchEngine");
        UrlDownloader downloader = (UrlDownloader) app.getBeanFactory().getBean("downloader");
        UrlParser parser         = (UrlParser) app.getBeanFactory().getBean("parser");
        LinkChecker checker      = (LinkChecker) app.getBeanFactory().getBean("checker");

        List<SearchResult> results = null;
        List<WebPage> webPages = null;
        List<LinkSet> linkSets = new ArrayList<LinkSet>();
        
        try {
            if (!isCancelled()) {
                setMessage(getResourceMap().getString("searchingProgress"));
                Thread.sleep(500);
                this.tag.setBinder(movie.toString());
                results  = engine.getResults(this.tag, this.page);
            }
            if (!isCancelled()) {
                setMessage(getResourceMap().getString("downloadingProgress"));
                webPages = downloader.download(results);
            }
            if (!isCancelled()) {
                setMessage(getResourceMap().getString("parsingProgress"));
                webPages = parser.parse(webPages);
                Thread.sleep(500);
            }
            if (!isCancelled()){
                setMessage(getResourceMap().getString("checkingLinkProgress"));
                for (WebPage webPage : webPages) {
                    for (LinkSet linkSet : webPage.getLinkSets()) {
                        try {
                            linkSet = checker.check(linkSet);
                            linkSets.add(linkSet);
                            publish(linkSet);
                        } catch (LinkCheckerException ex) {
                            publish(linkSet);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {

        } catch (ParsingException ex) {
            
        } catch (SearchEngineException ex) {

        }

        return linkSets;
    }

    @Override
    public void process(List<LinkSet> linkSets)
    {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.linksetTree.getModel().getRoot();

        int i = 1;
        for (LinkSet linkSet : linkSets)
        {
            double linkSetSize  = 0;
            int active       = 0;
            for (Link link : linkSet.getLinks())
            {
                linkSetSize    = linkSetSize+link.getSize();
                active += (link.isActive() == true) ? 1 : 0;
            }
            

            String linksCountTreeItem  = getResourceMap().getString("treeNodeLinksLabel")+" - "+linkSet.getLinks().size();
            String linkSetSizeTreeItem = getResourceMap().getString("treeNodeSizeLabel")+" - "+LinkSizeUtil.formatSize(linkSetSize);
            String activeTreeItem      = getResourceMap().getString("treeNodeActiveLabel")+" - ("+active+"/"+linkSet.getLinks().size()+")";

            DefaultMutableTreeNode linkSetNode = new DefaultMutableTreeNode();
            linkSetNode.setUserObject(linkSet);
            linkSetNode.add(new DefaultMutableTreeNode(linksCountTreeItem));
            linkSetNode.add(new DefaultMutableTreeNode(linkSetSizeTreeItem));
            linkSetNode.add(new DefaultMutableTreeNode(activeTreeItem));
            rootNode.add(linkSetNode);
            ++i;
        }

        TreeModel model = new DefaultTreeModel(rootNode);
        this.linksetTree.setModel(model);
    }

    @Override
    protected void cancelled()
    {
        int nextPage = ++this.page;
        setMessage(getResourceMap().getString("cancelledLabel"));
        RifasGUIView mainView = (RifasGUIView) ((RifasGUIApp) getApplication()).getMainView();
        mainView.setSearchMoreEnabled(nextPage > 1 && nextPage <= 4);
    }

    @Override
    protected void succeeded(List<LinkSet> linkSets)
    {
        int nextPage = ++this.page;
        RifasGUIView mainView = (RifasGUIView) ((RifasGUIApp) getApplication()).getMainView();
        mainView.setSearchMoreEnabled(nextPage > 1 && nextPage <= 4);
        mainView.disableSearchCancel();
    }

}
