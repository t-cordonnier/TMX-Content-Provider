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


public class EntryKey  {
    public final String file;
    public final String sourceText;
    public final String id;
    public final String prev;
    public final String next;
    public final String path;

    /**
     * When true, ignore the {@link #file} member when comparing EntryKeys.
     */
    private static boolean ignoreFileContext = false;

    public EntryKey(final String file, final String sourceText, final String id, final String prev,
            final String next, final String path) {
        this.file = file;
        this.sourceText = sourceText;
        this.id = id;
        this.prev = prev;
        this.next = next;
        this.path = path;
    }


}
