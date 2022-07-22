/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2022 Thomas Cordonnier
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

/**
 * This provider displays notes from /extra-notes.tmx into comments pane
 * 
 * @author Thomas Cordonnier
 */
public class TmxCommentsProvider implements ICommentProvider, IProjectEventListener {
    ProjectTMX notesTmx = null;

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
                    ProjectProperties config = Core.getProject().getProjectProperties();
                    File file = new File(config.getProjectRoot() + "/extra-notes.tmx");
                    if (file.exists()) {
                        notesTmx = new ProjectTMX(config.getSourceLanguage(), config.getTargetLanguage(),
                            config.isSentenceSegmentingEnabled(), file, checkOrphanedCallback);
                        Log.log("Extra notes : loaded /extra-notes.tmx");
                    } else {
                        Log.log("Extra notes : /extra-notes.tmx not found for this project");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Core.getComments().addCommentProvider(this, 100);
                break;
            case CLOSE:
                // set to null
                notesTmx = null;
                Core.getComments().removeCommentProvider(this);
        }
    }

    /**
     * Search comment for entry, display it if found
     */
    public String getComment(SourceTextEntry newEntry) {
        if (notesTmx == null) return null;
        TMXEntry te = notesTmx.getMultipleTranslation(newEntry.getKey());
        if (te == null) te = notesTmx.getDefaultTranslation(newEntry.getSrcText());
        if (te != null) return "T&A note: " + te.translation;
        return null;
    }
}
