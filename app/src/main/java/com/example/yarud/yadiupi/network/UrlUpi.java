package com.example.yarud.yadiupi.network;

public class UrlUpi {
    //
    //1. login
    //url:
     public static final String URL_LOGIN = "http://api.upi.edu/jwt/api/user/login";
    //param: username, password (nip dan password acs)
    //method: post
    //result: token
    //
    //2. Detail user (update)
    //url:
    public static final String URL_USER = "http://api.upi.edu/jwt/api/user";
    //param: Authorization = Bearer (token)
    //method: get
    //result: GELARNAMA,KODEDOSEN,STATUS (Dosen,Tendik,Mahasiswa,Lainya)
    //
    //3. logout
    //url: http://api.upi.edu/jwt/api/logout
    //param: Authorization = Bearer (token)
    //method: get
    //
    //4. penugasan (update)
    //url:
     public static final String URL_PENUGASAN = "http://api.upi.edu/jwt/api/penugasan/";
    //param: id=kodedsn
    //param: Authorization = Bearer (token)
    //method: get
    //result: IDMK,KODEKLS,NAMAKELAS,KODEMK,KODEDSN,NAMADSN,NAMAMK,SKS,NAMAPST,THNAJAR,SMTAJAR,JENJANG,NAMADSN2,NAMADSN3
    //
    //5. detil mk
    //url:
     public static final String URL_DetilMK = "http://api.upi.edu/jwt/api/detil_mk/";
    //param: id=idmk
    //param: Authorization = Bearer (token)
    //method: get
    //result: IDMK,KODEKLS,NAMAKELAS,KODEMK,KODEDSN,NAMADSN,NAMAMK,SKS,NAMAPST,THN,SMT,NAMAHR,JAM1,JAM2,KODERUANG,NAMARUANG
    //
    //6. risalah mk (update)
    //url:
     public static final String URL_RisalahMK = "http://api.upi.edu/jwt/api/risalah_mk/";
    //param: id=idmk
    //param: Authorization = Bearer (token)
    //method: get
    //result: id_rs,id_pn,pertemuan,topik,subtopik,sesuai,waktu,user_id,approve,tgl,finish,tgl_finish
    //
    //7. presensi
    //url:
     public static final String URL_Presensi = "http://api.upi.edu/jwt/api/presensi/";
    //param: idrs=idrisalah
    //param: Authorization = Bearer (token)
    //method: get
    //result: id_pr,id_pn,id_rs,nim,status,ket,nama
    //
    //
    //8. input presensi (new untuk dosen)
    //url:
     public static final String URL_InputPresensi = "http://api.upi.edu/jwt/api/input_presensi";
    //param: 	id_rs= id_risalah (string)
    //	nim = array/string
    //	status = array/string
    //	ket = array/string
    //param: Authorization = Bearer (token)
    //method: post
    //result:
    //
    //9. Cek KM (new untuk dosen)
    //url:
     public static final String URL_RisalahMK_Cek_KM = "http://api.upi.edu/jwt/api/check_km/";
    //param: id=id_penugasan/idmk
    //param: Authorization = Bearer (token)
    //method: get
    //result: 1(jika ada), 0(jika belum)
    //
    //10. Set KM (new untuk dosen)
    //url: http://api.upi.edu/jwt/api/set_km
    //param: id_pn=id_penugasan/idmk
    //	nim = nim mahasiswa
    //param: Authorization = Bearer (token)
    //method: post
    //
    //11. Approve kehadiran dosen (new untuk mahasiswa)
    //url: http://api.upi.edu/jwt/api/approve_dsn_hadir
    //param: idrs=idrisalah
    //param: Authorization = Bearer (token)
    //method: post
    //
    //
    //12. Mahasiswa Kontrak (new untuk mahasiswa)
    //url: http://api.upi.edu/jwt/api/mhs_kontrak
    //param: nim (untuk development, untuk produksi parameter nim dihilangkan, nim diambil dari user yang login)
    //param: Authorization = Bearer (token)
    //method: get
    //result:
    //"IDMK": "18100029792990IK300D05520181",
    //"KODEMK": "IK300",
    //"NAMAMK": "PENGANTAR TEKNOLOGI INFORMASI",
    //"SKS": "2.00",
    //"NIM": "1802285",
    //"KM": "1", ==> 1 jika sebagai KM, 0 jika bukan
    //"NEEDAPPROVE": 1 (jumlah risalah yang belum di approve)
    //
    //
    //13. Finish absen (new untuk dosen)
    //url: http://api.upi.edu/jwt/api/set_finish_absen
    //param: id_rs = id_risalah
    //param: Authorization = Bearer (token)
    //method: post
    //
    //14. Judul Diskusi (new untuk dosen dan mahasiswa)
    //url: http://api.upi.edu/jwt/api/judul_diskusi
    //param: idmk = idmk
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
    //
    //15. Diskusi (new untuk dosen dan mahasiswa)
    //url: http://api.upi.edu/jwt/api/diskusi
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
    //
    //16. Input diskusi (new untuk dosen dan mahasiswa)
    //url: http://api.upi.edu/jwt/api/input_diskusi
    //method: post
    //param:
    //	user_id (nip/nim, untuk development, untuk produksi parameter nim/nip dihilangkan, nim/nip diambil dari user yang login)
    //	idmk (id matakuliah)
    //	judul (judul diskusi/forum)
    //	isi (isi)
    //
    //17. Reply diskusi (new untuk dosen dan mahasiswa)
    //url: http://api.upi.edu/jwt/api/reply_diskusi
    //method: post
    //param:
    //	user_id (nip/nim, untuk development, untuk produksi parameter nim/nip dihilangkan, nim/nip diambil dari user yang login)
    //	idmk (id matakuliah)
    //	isi (isi)
    //	induk (id_frm)
    //
}
