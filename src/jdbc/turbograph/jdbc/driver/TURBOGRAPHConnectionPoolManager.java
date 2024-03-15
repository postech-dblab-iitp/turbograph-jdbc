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

package turbograph.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

abstract class TURBOGRAPHConnectionPoolManager {
    private static Hashtable<String, TURBOGRAPHConnectionEventListener> connectionPooltable;
    private static Hashtable<String, TURBOGRAPHConnectionPoolDataSource> poolDataSourceTable;

    static {
        connectionPooltable = new Hashtable<String, TURBOGRAPHConnectionEventListener>();
        poolDataSourceTable = new Hashtable<String, TURBOGRAPHConnectionPoolDataSource>();
    }

    static Connection getConnection(TURBOGRAPHConnectionPoolDataSource pds, String user, String passwd)
            throws SQLException {
        TURBOGRAPHConnectionEventListener cp;

        String key = pds.getDataSourceID(user);

        synchronized (connectionPooltable) {
            cp = connectionPooltable.get(key);

            if (cp == null) {
                cp = addConnectionPool(key, pds);
            }
        }

        return cp.getConnection(user, passwd);
    }

    static TURBOGRAPHConnectionPoolDataSource getConnectionPoolDataSource(String dsName)
            throws SQLException {
        TURBOGRAPHConnectionPoolDataSource cpds;

        synchronized (poolDataSourceTable) {
            cpds = poolDataSourceTable.get(dsName);

            if (cpds == null) {
                try {
                    Context ctx = new InitialContext();
                    cpds = (TURBOGRAPHConnectionPoolDataSource) ctx.lookup(dsName);
                } catch (NamingException e) {
                    throw new TURBOGRAPHException(TURBOGRAPHJDBCErrorCode.unknown, e.toString(), e);
                }

                poolDataSourceTable.put(dsName, cpds);
            }
        }

        return cpds;
    }

    private static TURBOGRAPHConnectionEventListener addConnectionPool(
            String key, TURBOGRAPHConnectionPoolDataSource pds) {
        TURBOGRAPHConnectionEventListener cp = new TURBOGRAPHConnectionEventListener(pds);
        connectionPooltable.put(key, cp);
        return cp;
    }
}
