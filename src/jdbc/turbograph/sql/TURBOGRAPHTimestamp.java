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

package turbograph.sql;

import java.sql.Timestamp;

public class TURBOGRAPHTimestamp extends Timestamp {
    private static final long serialVersionUID = 8413186757763082411L;

    public static final boolean TIMESTAMP = false;
    public static final boolean DATETIME = true;

    boolean isDatetime = true;

    public TURBOGRAPHTimestamp(long time, boolean isDatetime) {
        super(time);
        this.isDatetime = isDatetime;
    }

    public static TURBOGRAPHTimestamp valueOf(String s, boolean isdt) {
        Timestamp tmptime = Timestamp.valueOf(s);
        TURBOGRAPHTimestamp cub_tmptime = new TURBOGRAPHTimestamp(tmptime.getTime(), isdt);
        return cub_tmptime;
    }

    public static boolean isTimestampType(Timestamp o) {
        if (o instanceof TURBOGRAPHTimestamp) {
            if (!((TURBOGRAPHTimestamp) o).isDatetime) {
                return true;
            }
        }
        return false;
    }
}
