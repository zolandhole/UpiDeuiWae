package com.yandi.yarud.yadiupi.utility.network;

public class UrlUpi {
    //1. login
     public static final String URL_LOGIN = "http://api.upi.edu/jwt/api/user/login";
    //param: username, password (nip dan password acs)
    //method: post

    //2. Detail user (update)
    public static final String URL_USER = "http://api.upi.edu/jwt/api/user";
    //param: Authorization = Bearer (token)
    //method: get

    //3. logout
    //url: http://api.upi.edu/jwt/api/logout
    //param: Authorization = Bearer (token)
    //method: get

    //4. penugasan (update)
     public static final String URL_PENUGASAN = "http://api.upi.edu/jwt/api/penugasan/";
    //param: id=kodedsn
    //param: Authorization = Bearer (token)
    //method: get

    //5. detil mk
     public static final String URL_DetilMK = "http://api.upi.edu/jwt/api/detil_mk/";
    //param: id=idmk
    //param: Authorization = Bearer (token)
    //method: get

    //6. risalah mk (update)
     public static final String URL_RisalahMK = "http://api.upi.edu/jwt/api/risalah_mk/";
    //param: id=idmk
    //param: Authorization = Bearer (token)
    //method: get

    //7. presensi
     public static final String URL_Presensi = "http://api.upi.edu/jwt/api/presensi/";
    //param: idrs=idrisalah
    //param: Authorization = Bearer (token)
    //method: get

    //8. input presensi (new untuk dosen)
     public static final String URL_InputPresensi = "http://api.upi.edu/jwt/api/input_presensi";
    //  param: 	id_rs= id_risalah (string)
    //	nim = array/string
    //	status = array/string
    //	ket = array/string
    //param: Authorization = Bearer (token)
    //method: post

    //9. Cek KM (new untuk dosen)
     public static final String URL_RisalahMK_Cek_KM = "http://api.upi.edu/jwt/api/check_km/";
    //param: id=id_penugasan/idmk
    //param: Authorization = Bearer (token)
    //method: get

    //10. Set KM (new untuk dosen)
     public static final String URL_SetKM = "http://api.upi.edu/jwt/api/set_km";
    //param: id_pn=id_penugasan/idmk
    //	nim = nim mahasiswa
    //param: Authorization = Bearer (token)
    //method: post

    //11. Approve kehadiran dosen (new untuk mahasiswa)
     public static final String URL_ApproveKehadiranDosen = "http://api.upi.edu/jwt/api/approve_dsn_hadir";
    //param: idrs=idrisalah
    //param: Authorization = Bearer (token)
    //method: post

    //12. Mahasiswa Kontrak (new untuk mahasiswa)
     public static final String URL_MahasiswaKontrak = "http://api.upi.edu/jwt/api/mhs_kontrak/";
    //param: nim (untuk development, untuk produksi parameter nim dihilangkan, nim diambil dari user yang login)
    //param: Authorization = Bearer (token)
    //method: get

    //13. Finish absen (new untuk dosen)
    public static final String URL_FinishAbsen = "http://api.upi.edu/jwt/api/set_finish_absen";
    //param: id_rs = id_risalah
    //param: Authorization = Bearer (token)
    //method: post

    //14. Judul Diskusi (new untuk dosen dan mahasiswa)
    public static final String URL_DataForum = "http://api.upi.edu/jwt/api/judul_diskusi/";
    //param: idmk = idmk
    //param: Authorization = Bearer (token)
    //method: get

    //15. Diskusi (new untuk dosen dan mahasiswa)
    public static final String URL_Diskusi = "http://api.upi.edu/jwt/api/diskusi/";
    //param: induk = induk (id_frm)
    //param: Authorization = Bearer (token)
    //method: get
    //result:
    //			"id_frm": 12967,
    //            "id_pn": "18100032781581GD541K06520181",
    //            "user_id": "195910221985031008",
    //            "judul": "Apakah hakekat manusia itu?",
    //            "isi": "Setiap mhs hrs merespon spy dicatat oleh sistem?",
    //            "induk": 0,
    //            "waktu": "2018-09-20 07:32:24",
    //            "nama": "Drs. H. Herli Salim, M.Ed., Ph.D."

    //16. Input diskusi (new untuk dosen dan mahasiswa)
    public static final String URL_InputDiskusi = "http://api.upi.edu/jwt/api/input_diskusi";

    //method: post
    //param:
    //	user_id (nip/nim, untuk development, untuk produksi parameter nim/nip dihilangkan, nim/nip diambil dari user yang login)
    //	idmk (id matakuliah)
    //	judul (judul diskusi/forum)
    //	isi (isi)

    //17. Reply diskusi (new untuk dosen dan mahasiswa)
    public static final String URL_ReplyDiskusi = "http://api.upi.edu/jwt/api/reply_diskusi";
    //method: post
    //param:
    //	user_id (nip/nim, untuk development, untuk produksi parameter nim/nip dihilangkan, nim/nip diambil dari user yang login)
    //	idmk (id matakuliah)
    //	isi (isi)
    //	induk (id_frm)
}
