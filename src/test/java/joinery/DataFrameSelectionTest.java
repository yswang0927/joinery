/*
 * Joinery -- Data frames for Java
 * Copyright (c) 2014, 2015 IBM Corp.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package joinery;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class DataFrameSelectionTest {
    private DataFrame<Object> df;

    @Before
    public void setUp()
    throws Exception {
        final List<List<Object>> data = new ArrayList<>();
        final Collection<String> rows = new ArrayList<>();
        final Collection<String> cols = Arrays.<String>asList("name", "value");
        final List<Object> names = new ArrayList<>();
        final List<Object> values = new ArrayList<>();
        for (int i = 0; i < 200; i += 10) {
            rows.add(String.format("row%d", i / 10));
            names.add(UUID.randomUUID().toString());
            values.add(i);
        }
        data.add(names);
        data.add(values);

        df = new DataFrame<>(rows, cols, data);
    }

    @Test
    public void testSelectMatch() {
        assertEquals(
                1,
                df.select(new DataFrame.Predicate<Object>() {
                    @Override
                    public Boolean apply(final List<Object> row) {
                        return new Integer(150).equals(row.get(1));
                    }
                }).length()
            );
    }

    @Test
    public void testSelectNoMatch() {
        assertEquals(
                0,
                df.select(new DataFrame.Predicate<Object>() {
                    @Override
                    public Boolean apply(final List<Object> row) {
                        return false;
                    }
                }).length()
            );
    }

    @Test
    public void testSelectIndex() {
        assertArrayEquals(
                new String[] { "row15" },
                df.select(new DataFrame.Predicate<Object>() {
                    @Override
                    public Boolean apply(final List<Object> row) {
                        return new Integer(150).equals(row.get(1));
                    }
                }).index().toArray()
            );
    }
}
