/*
 * Copyright (c) 2019.
 *
 * This file is part of Sable.
 *
 * Sable is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sable is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sable.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.sable.utilities;

import com.sable.BaseTest;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringReplacementTests extends BaseTest {

    @Test
    public void testReplaceAllReplacesFoundKeysCorrectly() {
        assertEquals("Hello, Ava", StringReplacementUtil.replaceAll(
            "Hello, World", "World", "Ava"
        ));
    }

    @Test
    public void testReplaceAllReplacesSpecialCharacters() {
        assertEquals("Hello, Ava!", StringReplacementUtil.replaceAll(
            "Hello, :name!", ":name", "Ava"
        ));
        assertEquals("Hello, Ava!", StringReplacementUtil.replaceAll(
            "Hello, [:&%s]!", "[:&%s]", "Ava"
        ));
        assertEquals("Hello, Ava!", StringReplacementUtil.replaceAll(
            "Hello, [:&%s]!", "[:&%s]", "Ava"
        ));
        assertEquals("Hello, Ava!", StringReplacementUtil.replaceAll(
            "Hello, ${:name}!", "${:name}", "Ava"
        ));
    }

    @Test
    public void testReplaceAllOccurrencesOfKey() {
        assertEquals("PotatoPotatoPotato", StringReplacementUtil.replaceAll(
            "...", ".", "Potato"
        ));
        assertEquals("-Sable-", StringReplacementUtil.replaceAll(
            "%Sable%", "%", "-"
        ));
    }
}
