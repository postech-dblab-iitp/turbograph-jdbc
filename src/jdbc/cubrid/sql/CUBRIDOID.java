/*
 * Copyright (C) 2008 Search Solution Corporation.
 * Copyright (c) 2016 CUBRID Corporation.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of the <ORGANIZATION> nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */

package cubrid.sql;

/**
 * Title: CUBRID JDBC Driver Description:
 *
 * @version 2.0
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface CUBRIDOID {

    public ResultSet getValues(String attrNames[]) throws SQLException;

    public void setValues(String[] attrNames, Object[] values) throws SQLException;

    public void remove() throws SQLException;

    public boolean isInstance() throws SQLException;

    public void setReadLock() throws SQLException;

    public void setWriteLock() throws SQLException;

    public void addToSet(String attrName, Object value) throws SQLException;

    public void removeFromSet(String attrName, Object value) throws SQLException;

    public void addToSequence(String attrName, int index, Object value) throws SQLException;

    public void putIntoSequence(String attrName, int index, Object value) throws SQLException;

    public void removeFromSequence(String attrName, int index) throws SQLException;

    public String getOidString() throws SQLException;

    public String getTableName() throws SQLException;

    public byte[] getOID();

    public Connection getConnection();
}
