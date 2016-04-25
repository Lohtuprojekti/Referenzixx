package referenzixx.database;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import referenzixx.parser.BibtexReader;
import referenzixx.parser.BibtexWriter;
import referenzixx.refs.ReferenceEntryBuilder;
import referenzixx.ui.ReferencePanel;

/**
 *
 */
public class DatabaseUtils implements ReferenceDatabase {

    // DONE: read bibtex file to an internal hashmap, within constructor
    // DONE: file location can be specified in a constructor parameter
    // TODO: 
    // implement interface methods for filtering out unwanted articles.
    private File file;
    private BibTeXDatabase database;

    /**
     * Default constructor called by MainUI.
     */
    public DatabaseUtils() {
        this("referenzixx.bib");
    }

    /**
     * Constructor of class DatabaseUtils
     *
     * @param url
     */
    public DatabaseUtils(String url) { //Käli haluaa url version
        this(new File(url));
    }

    /**
     * Constructor of class DatabaseUtils
     *
     * @param file
     */
    public DatabaseUtils(File file) {
        this.selectFile(file);
    }

    /**
     * Changes the currently accessed file and database to given file.
     *
     * @param url
     */
    public void selectFile(String url) { //Käli haluaa url version
        this.selectFile(new File(url));
    }

    /**
     * Changes the currently accessed file and database to given file.
     *
     * @param file
     */
    private void selectFile(File file) {
        this.file = file;
        this.database = new BibtexReader().openNewFile(this.file);
    }

    /**
     * Adds an entry to the file and BibTeXDatabase
     *
     * @param entry
     */
    public void addEntry(BibTeXEntry entry) {
        if (entry == null || !isRefnumUnique(entry.getKey(), database)) {
            return;
        }
        database.addObject(entry);
        new BibtexWriter().writeToBibtex(entry, file);
    }

    public void addEntry(Key type, String ref, List<ReferencePanel> references) {
        ReferenceEntryBuilder builder = new ReferenceEntryBuilder();
        BibTeXEntry entry = new ReferenceEntryBuilder().buildEntry(type, ref, database, references);
        this.addEntry(entry);
    }

    /**
     * Deletes an entry from BibTexDatabase
     *
     * @param entry
     */
    private void delEntry(BibTeXEntry entry) {
        database.removeObject(entry);
    }

    /**
     * Copies the currently chosen bibtex file to clipboard.
     */
    public void copyToClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String content = new BibtexReader().getBibFileAsString(file);
        clipboard.setContents(new StringSelection(content), null);
    }

    /**
     * Returns the database as a list of BibTeXEntry
     *
     * @return
     */
    @Override
    public List<BibTeXEntry> getReferences() {
        List<BibTeXEntry> entryList = new ArrayList<>();

        for (BibTeXEntry entry : database.getEntries().values()) {
            entryList.add(entry);
        }
        return entryList;
    }

    /**
     *
     * @param filters
     * @return
     */
    @Override
    public List<BibTeXEntry> getReferences(Map<String, String> filters) {
        List<BibTeXEntry> entryList = new ArrayList<>();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            for (BibTeXEntry entry : database.getEntries().values()) {
                if (entry.getFields().containsKey(new Key(filter.getKey()))) {
                    if (entry.getField(new Key(filter.getKey())).toUserString()
                            .equals(filter.getValue())) {
                        entryList.add(entry);
                    }
                }
            }
        }
        return entryList;
    }

    /**
     * Checks if the reference ID of the BibTexEntry is unique
     *
     * @param refnum
     * @param database
     * @return
     */
    private boolean isRefnumUnique(Key refnum, BibTeXDatabase database) {
        return !database.getEntries().containsKey(refnum);
    }

    /**
     * Used to get the currently chosen file.
     *
     * @return File that is edited
     */
    public File getFile() {
        return file;
    }

    /**
     * Used to get the currently chosen database.
     *
     * @return
     */
    public BibTeXDatabase getDatabase() {
        return database;
    }
}
