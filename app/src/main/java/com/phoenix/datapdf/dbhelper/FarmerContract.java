package com.phoenix.datapdf.dbhelper;

import android.provider.BaseColumns;

public final class FarmerContract {

    private FarmerContract() {
    }

    public static class FarmerTable implements BaseColumns {
        public static final String TABLE_NAME = "farmer_table";
        public static final String COL_NAME = "name";
        public static final String COL_REGNO = "regno";
        public static final String COL_SONOF = "sonof";
        public static final String COL_DOB = "dob";
        public static final String COL_PHONE = "phone";
        public static final String COL_AADHAR = "aadhar";
        public static final String COL_BLOCK = "block";
        public static final String COL_VILLAGE = "village";
        public static final String COL_PANCHAYAT = "panchayat";
        public static final String COL_DISTRICT = "district";
    }
}
