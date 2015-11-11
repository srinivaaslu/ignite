/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.query.h2.opt;

import org.h2.result.RowFactory;
import org.h2.value.Value;

/**
 * Row factory.
 */
public class GridH2RowFactory extends RowFactory {
    /** */
    public static final GridH2RowFactory INSTANCE = new GridH2RowFactory();

    /**
     * @param v Value.
     * @return Row.
     */
    public GridH2Row create(Value v) {
        return new RowKey(v);
    }

    /**
     * @param v1 Value 1.
     * @param v2 Value 2.
     * @return Row.
     */
    public GridH2Row create(Value v1, Value v2) {
        return new RowPair(v1, v2);
    }

    /** {@inheritDoc} */
    @Override public GridH2Row createRow(Value[] data, int memory) {
        switch (data.length) {
            case 1:
                return new RowKey(data[0]);

            case 2:
                return new RowPair(data[0], data[1]);

            default:
                return new RowSimple(data);
        }
    }

    /**
     * Single value row.
     */
    private static final class RowKey extends GridH2Row {
        /** */
        private Value key;

        /**
         * @param key Key.
         */
        public RowKey(Value key) {
            this.key = key;
        }

        /** {@inheritDoc} */
        @Override public int getColumnCount() {
            return 1;
        }

        /** {@inheritDoc} */
        @Override public Value getValue(int idx) {
            assert idx == 0 : idx;
            return key;
        }

        /** {@inheritDoc} */
        @Override public void setValue(int index, Value v) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        @Override public Value[] getValueList() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Row of two values.
     */
    private static final class RowPair extends GridH2Row  {
        /** */
        private Value v1;

        /** */
        private Value v2;

        /**
         * @param v1 First value.
         * @param v2 Second value.
         */
        private RowPair(Value v1, Value v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        /** {@inheritDoc} */
        @Override public int getColumnCount() {
            return 2;
        }

        /** {@inheritDoc} */
        @Override public Value getValue(int idx) {
            return idx == 0 ? v1 : v2;
        }

        /** {@inheritDoc} */
        @Override public void setValue(int index, Value v) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        @Override public Value[] getValueList() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Simple array based row.
     */
    private static final class RowSimple extends GridH2Row {
        /** */
        private Value[] vals;

        /**
         * @param vals Values.
         */
        private RowSimple(Value[] vals) {
            this.vals = vals;
        }

        /** {@inheritDoc} */
        @Override public Value[] getValueList() {
            return vals;
        }

        /** {@inheritDoc} */
        @Override public int getColumnCount() {
            return vals.length;
        }

        /** {@inheritDoc} */
        @Override public Value getValue(int idx) {
            return vals[idx];
        }

        /** {@inheritDoc} */
        @Override public void setValue(int idx, Value v) {
            throw new UnsupportedOperationException();
        }
    }
}
