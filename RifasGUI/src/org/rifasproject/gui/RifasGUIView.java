/*
 * RifasGUIView.java
 */

package org.rifasproject.gui;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jdesktop.application.Application;
import org.rifasproject.domain.MovieType;
import org.rifasproject.gui.listener.LinksetTreeExpansionListener;
import org.rifasproject.gui.listener.TableRowDubleClickListener;
import org.rifasproject.gui.task.SearchLinksTask;
import org.rifasproject.gui.util.LinkTableCellRenderer;
import org.rifasproject.util.LinkComparator;
import org.rifasproject.util.RegexRepository;
import org.rifasproject.gui.util.SizeTableCellRenderer;

/**
 * The application's main frame.
 */
public class RifasGUIView extends FrameView {

    private boolean searchMoreEnabled   = false;
    private boolean exportEnabled       = false;
    private boolean searchEnabled       = true;
    private final int FIRST_PAGE = 1;
    private int page = FIRST_PAGE;

    private Task searchTask;

    public RifasGUIView(SingleFrameApplication app) {
        super(app);
        getFrame().setResizable(false);
        getFrame().setIconImage(getResourceMap().getImageIcon("mainApp.icon").getImage());
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        movieLabel = new javax.swing.JLabel();
        movieName = new javax.swing.JTextField();
        movieType = new javax.swing.JComboBox();
        movieYear = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        linksetTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        linksTable = new javax.swing.JTable();
        moreButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        searchString = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.rifasproject.gui.RifasGUIApp.class).getContext().getResourceMap(RifasGUIView.class);
        movieLabel.setText(resourceMap.getString("movieLabel.text")); // NOI18N
        movieLabel.setName("movieLabel"); // NOI18N

        movieName.setToolTipText(resourceMap.getString("movieName.toolTipText")); // NOI18N
        movieName.setName("movieName"); // NOI18N

        movieType.setModel(new javax.swing.DefaultComboBoxModel(org.rifasproject.domain.MovieType.values()));
        movieType.setToolTipText(resourceMap.getString("movieType.toolTipText")); // NOI18N
        movieType.setName("movieType"); // NOI18N

        movieYear.setToolTipText(resourceMap.getString("movieYear.toolTipText")); // NOI18N
        movieYear.setName("movieYear"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.rifasproject.gui.RifasGUIApp.class).getContext().getActionMap(RifasGUIView.class, this);
        searchButton.setAction(actionMap.get("searchLinks")); // NOI18N
        searchButton.setText(resourceMap.getString("searchButton.text")); // NOI18N
        searchButton.setName("searchButton"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(getResourceMap().getString("linksetTree.rootNodeText")));
        linksetTree.setModel(treeModel);
        linksetTree.addTreeExpansionListener(new LinksetTreeExpansionListener(this.linksTable));
        linksetTree.setName("linksetTree"); // NOI18N
        jScrollPane1.setViewportView(linksetTree);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        linksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Link", "Size"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        linksTable.setName("linksTable"); // NOI18N
        jScrollPane2.setViewportView(linksTable);
        linksTable.getColumnModel().getColumn(0).setResizable(false);
        linksTable.getColumnModel().getColumn(0).setPreferredWidth(450);
        linksTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("linksTable.columnModel.title0")); // NOI18N
        linksTable.getColumnModel().getColumn(1).setResizable(false);
        linksTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("linksTable.columnModel.title1")); // NOI18N
        linksTable.getColumnModel().getColumn(0).setCellRenderer(new LinkTableCellRenderer());
        linksTable.getColumnModel().getColumn(1).setCellRenderer(new SizeTableCellRenderer());
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(linksTable.getModel());
        sorter.setComparator(0, new LinkComparator(RegexRepository.RAPIDSHARE_LINK));
        linksTable.setRowSorter(sorter);
        linksTable.addMouseListener(new TableRowDubleClickListener());

        moreButton.setAction(actionMap.get("searchMoreLinks")); // NOI18N
        moreButton.setText(resourceMap.getString("moreButton.text")); // NOI18N
        moreButton.setName("moreButton"); // NOI18N

        exportButton.setAction(actionMap.get("exportLinks")); // NOI18N
        exportButton.setText(resourceMap.getString("exportButton.text")); // NOI18N
        exportButton.setName("exportButton"); // NOI18N

        searchString.setEditable(false);
        searchString.setFont(resourceMap.getFont("searchString.font")); // NOI18N
        searchString.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchString.setText(resourceMap.getString("searchString.text")); // NOI18N
        searchString.setName("searchString"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(movieLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(movieName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(movieType, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(movieYear, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchString))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(moreButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                .addComponent(exportButton)
                .addGap(257, 257, 257))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(movieLabel)
                    .addComponent(movieName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(movieType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(movieYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton)
                    .addComponent(searchString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportButton)
                    .addComponent(moreButton))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        cancelButton.setAction(actionMap.get("cancelSearch")); // NOI18N
        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.setVisible(false);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 471, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addComponent(cancelButton)))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = RifasGUIApp.getApplication().getMainFrame();
            aboutBox = new RifasGUIAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        RifasGUIApp.getApplication().show(aboutBox);
    }

    @Action(block = Task.BlockingScope.ACTION, enabledProperty="searchEnabled")
    public Task searchLinks() {
        Application app = RifasGUIApp.getApplication();
        this.searchTask = new SearchLinksTask(app,
                                               this.movieName.getText(),
                                               ((MovieType) this.movieType.getSelectedItem()),
                                               this.movieYear.getText(),
                                               this.searchString,
                                               this.linksetTree,
                                               FIRST_PAGE);
        this.updateSearchProperties();
        this.setExportEnabled(false);
        return this.searchTask;
    }

    @Action(block = Task.BlockingScope.ACTION, enabledProperty = "searchMoreEnabled")
    public Task searchMoreLinks() {
        Application app = RifasGUIApp.getApplication();
        this.searchTask = new SearchLinksTask(app,
                                           this.movieName.getText(),
                                           ((MovieType) this.movieType.getSelectedItem()),
                                           this.movieYear.getText(),
                                           this.searchString,
                                           this.linksetTree,
                                           this.page);
        this.updateSearchProperties();
        this.setExportEnabled(false);
        return this.searchTask;
    }

    @Action
    public void cancelSearch() {
        if (this.searchTask != null && !this.searchTask.isDone())
        {
            this.searchTask.cancel(true);
            this.disableSearchCancel();
            this.searchTask = null;
        }
    }


    @Action(block = Task.BlockingScope.WINDOW, enabledProperty = "exportEnabled")
    public void exportLinks() {
        if (this.exportDialog == null) {
            JFrame mainFrame = RifasGUIApp.getApplication().getMainFrame();
            this.exportDialog = new ExportDialog(mainFrame);
            this.exportDialog.setLocationRelativeTo(mainFrame);
        }
        ((ExportDialog) this.exportDialog).fillLinkExportWithLinks(this.linksTable);
        RifasGUIApp.getApplication().show(this.exportDialog);
    }

    /**
     * Property modifiers
     */
    private void updateSearchProperties() {
        ++this.page;
    }

    public boolean isSearchEnabled()
    {
        return this.searchEnabled;
    }

    public void setSearchEnabled(boolean searchEnabled)
    {
        boolean old = this.isSearchEnabled();
        this.searchEnabled = searchEnabled;
        firePropertyChange("searchEnabled", old, isSearchEnabled());
    }

    public boolean isSearchMoreEnabled() {
        return searchMoreEnabled;
    }

    public void setSearchMoreEnabled(boolean b) {
        boolean old = isSearchMoreEnabled();
        this.searchMoreEnabled = b;
        firePropertyChange("searchMoreEnabled", old, isSearchMoreEnabled());
        setSearchEnabled(isSearchMoreEnabled() == true);
    }

    public boolean isExportEnabled() {
        return exportEnabled;
    }

    public void setExportEnabled(boolean exportEnabled) {
        boolean old = isExportEnabled();
        this.exportEnabled = exportEnabled;
        firePropertyChange("exportEnabled", old, isExportEnabled());
    }

    public void enableSearchCancel()
    {
        this.cancelButton.setVisible(true);
    }
    public void disableSearchCancel()
    {
        this.cancelButton.setVisible(false);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable linksTable;
    private javax.swing.JTree linksetTree;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton moreButton;
    private javax.swing.JLabel movieLabel;
    private javax.swing.JTextField movieName;
    private javax.swing.JComboBox movieType;
    private javax.swing.JTextField movieYear;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchString;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private JDialog exportDialog;
}