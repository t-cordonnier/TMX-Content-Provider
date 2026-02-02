/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2022-2023 Thomas Cordonnier
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.gui.comments;

import java.io.File;
import java.util.Set;
import java.util.HashSet;

import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TMXEntry;
import org.omegat.core.data.ProjectTMX;
import org.omegat.core.data.EntryKey;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.core.CoreEvents;
import org.omegat.core.Core;
import org.omegat.util.Log;
import org.omegat.util.Language;
import org.omegat.util.Preferences;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

/**
 * This provider displays notes from /extra-notes.tmx into comments pane
 * 
 * @author Thomas Cordonnier
 */
public class TmxCommentsProvider implements ICommentProvider, IProjectEventListener {
    ProjectTMX notesTmx = null;
    ProjectProperties config = null;
    File tmxFile = null;
    Language srcLang, traLang;

    private Set<String> existSource = new HashSet<>();
    private Set<EntryKey> existKeys = new HashSet<>();
    ProjectTMX.CheckOrphanedCallback checkOrphanedCallback = new ProjectTMX.CheckOrphanedCallback() {
        public boolean existSourceInProject(String src) {
            return existSource.contains(src);
        }

        public boolean existEntryInProject(EntryKey key) {
            return existKeys.contains(key);
        }
    };

    public TmxCommentsProvider() {
        CoreEvents.registerProjectChangeListener(this);
    }

    public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
        switch (eventType) {
            case CREATE: case LOAD:
                try {
                    config = Core.getProject().getProjectProperties();
                    File file = new File(config.getProjectRoot() + "/notes");
                    if (file.exists()) {
                        File[] list = file.listFiles(file0 -> file0.getName().toLowerCase().endsWith(".tmx"));
                        if (list.length == 0)
                            Log.log("Extra notes : /notes directory exists but contains no file");
                        else {
                            srcLang = config.getSourceLanguage(); traLang = config.getTargetLanguage();
                            loadTmxFile(tmxFile = list[0]);
                            Core.getComments().addCommentProvider(this, 100);
                        }
                    } else {
                        Log.log("Extra notes : /notes directory not found for this project");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case CLOSE:
                // set to null
                notesTmx = null;
                Core.getComments().removeCommentProvider(this);
        }
    }

    private void loadTmxFile(File tmxFile) throws Exception {
        notesTmx = new ProjectTMX(srcLang, traLang,
            Core.getProject().getProjectProperties().isSentenceSegmentingEnabled(), tmxFile, checkOrphanedCallback);
    }

    private final XMLInputFactory factory = XMLInputFactory.newInstance();

    private void checkTmxFileLanguages() throws Exception {
        Log.log("Try to identify language for " + tmxFile);
        XMLStreamReader reader = factory.createXMLStreamReader(new java.io.FileInputStream(tmxFile));
        this.srcLang = this.traLang = null;
        while (reader.hasNext())
            if (reader.next() == XMLStreamReader.START_ELEMENT)
                if (reader.getLocalName().equals("tuv")) {
                    String attr = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
                    if (attr == null) attr = reader.getAttributeValue(null, "lang");

                    if (this.srcLang == null) this.srcLang = new Language(attr);
                    else if (this.traLang == null) {
                        this.traLang = new Language(attr);
                        Log.log("" + tmxFile + " : identified languages " + this.srcLang + " / " + this.traLang);
                        reader.close(); return; // no need to load other entries
                    }
                }
    }

    /**
     * Search comment for entry, display it if found
     */
    public String getComment(SourceTextEntry newEntry) {
        if (notesTmx == null) return null;
        TMXEntry te = notesTmx.getMultipleTranslation(newEntry.getKey());
        if (te == null) te = notesTmx.getDefaultTranslation(newEntry.getSrcText());
        if (te != null) {
            if (te.translation == null)
                try {
                    if ((! this.srcLang.equals(config.getSourceLanguage()))
                     || (! this.traLang.equals(config.getTargetLanguage())))
                        return null; // already tried to reload, don't try again
                    checkTmxFileLanguages(); loadTmxFile(tmxFile);
                    return getComment(newEntry);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            return Preferences.getPreferenceDefault("tmx_content_provider_id", "T&A note: ") + te.translation;
        }
        return null;
    }
}
