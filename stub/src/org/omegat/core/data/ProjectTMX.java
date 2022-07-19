/**************************************************************************
 Copyright (C) 2022 Silvestris project (http://www.silvestris-lab.org)

 This file is only a stub to enable compilation of Extra notes plugin

 DGT-OmegaT is free software: you can redistribute it and/or modify
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
package org.omegat.core.data;

import java.io.File;

import org.omegat.util.Language;


public class ProjectTMX {


    public ProjectTMX(Language sourceLanguage, Language targetLanguage, boolean isSentenceSegmentingEnabled,
            File file, CheckOrphanedCallback callback) throws Exception {

    }

    /**
     * Get default translation or null if not exist.
     */
    public TMXEntry getDefaultTranslation(String source) {
        return null;
    }

    /**
     * Get multiple translation or null if not exist.
     */
    public TMXEntry getMultipleTranslation(EntryKey ek) {
        return null;
    }

    public interface CheckOrphanedCallback {
        boolean existEntryInProject(EntryKey key);

        boolean existSourceInProject(String src);
    }

}
