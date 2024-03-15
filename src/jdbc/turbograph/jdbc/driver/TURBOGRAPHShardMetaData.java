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

import java.sql.SQLException;

import turbograph.jdbc.jci.UConnection;
import turbograph.jdbc.jci.UError;
import turbograph.jdbc.jci.UErrorCode;
import turbograph.jdbc.jci.UShardInfo;

/**
 * Title: CUBRID JDBC Driver Description:
 *
 * @version 2.0
 */
public class TURBOGRAPHShardMetaData {
    TURBOGRAPHConnection con;
    UConnection u_con;
    UError error;
    boolean is_closed;
    TURBOGRAPHDatabaseMetaData[] metadata = null;

    protected TURBOGRAPHShardMetaData(TURBOGRAPHConnection c) {
        con = c;
        u_con = con.u_con;
        error = null;
        is_closed = false;
        metadata = null;
    }

    synchronized void close() {
        is_closed = true;
        con = null;
        u_con = null;
        error = null;
        metadata = null;
    }

    private void checkIsOpen() throws SQLException {
        if (is_closed) {
            throw con.createTURBOGRAPHException(TURBOGRAPHJDBCErrorCode.dbmetadata_closed, null);
        }
    }

    public synchronized TURBOGRAPHDatabaseMetaData getMetaData(int shard_id) throws SQLException {
        int num_shard;
        UShardInfo shard_info;

        checkIsOpen();

        if (metadata != null) {
            shard_info = u_con.getShardInfo(shard_id);
            switch (error.getErrorCode()) {
                case UErrorCode.ER_NO_ERROR:
                    break;
                default:
                    throw con.createTURBOGRAPHHException(error);
            }

            return metadata[shard_id];
        }

        synchronized (u_con) {
            num_shard = u_con.shardInfo();

            error = u_con.getRecentError();
            switch (error.getErrorCode()) {
                case UErrorCode.ER_NO_ERROR:
                    break;
                default:
                    throw con.createTURBOGRAPHHException(error);
            }
        }

        metadata = new TURBOGRAPHDatabaseMetaData[num_shard];
        for (int i = 0; i < num_shard; i++) {
            metadata[i] = new TURBOGRAPHDatabaseMetaData(con);

            shard_info = u_con.getShardInfo(i);
            error = u_con.getRecentError();
            switch (error.getErrorCode()) {
                case UErrorCode.ER_NO_ERROR:
                    break;
                default:
                    metadata = null;
                    throw con.createTURBOGRAPHHException(error);
            }

            metadata[i].setShardId(shard_info.getShardId());
        }

        shard_info = u_con.getShardInfo(shard_id);
        switch (error.getErrorCode()) {
            case UErrorCode.ER_NO_ERROR:
                break;
            default:
                throw con.createTURBOGRAPHHException(error);
        }

        return metadata[shard_id];
    }

    public synchronized int getShardCount() throws SQLException {
        checkIsOpen();

        int count = 0;
        synchronized (u_con) {
            count = u_con.getShardCount();
            error = u_con.getRecentError();
        }

        switch (error.getErrorCode()) {
            case UErrorCode.ER_NO_ERROR:
                break;
            default:
                throw con.createTURBOGRAPHHException(error);
        }

        return count;
    }
}
