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

package org.omegat.core.events;

/**
 * Listener interface for project status change events.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public interface IProjectEventListener {
    /** Event type which happen for project. */
    enum PROJECT_CHANGE_TYPE {
        CLOSE, COMPILE, CREATE, LOAD, SAVE, MODIFIED
    }

    /**
     * This method called when project status changed, i.e.
     * new/open/save/close/compile.
     */
    void onProjectChanged(PROJECT_CHANGE_TYPE eventType);
}
