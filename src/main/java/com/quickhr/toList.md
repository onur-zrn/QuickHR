# dashboard da verileri db den mi çekilecek yoksa koddan çekilecek onu kararlaştır (db den alınması daha mantıklı)

# üyelik tipi eklenecek

# companyId belirtilecek!

# üyelikler şirket maili ile olacak
# şirket mail onayı endpointler ile de yapılabilir (mail de linke tıklayınca olacak şeyi ayrı bir endpoint ile yapılabilir)

# Dashboard ve Visitor'un HOMEPAGE_CONTENT, PLATFORM_FEATURES ve HOW_IT_WORKS endPoints DB'den çekilecek
# Visitor da HOLIDAY için sadece şuanki yılın tatillerinin gelmesi yeterli olur

# Şirket yöneticisi employee ekleyecek employeede bir user. user role personel olacak.
# ayrıca personel info oluşacak ayrı bir şekilde. userid den ulaşılacak

# visitorde yazı yazabilirsin

# admin -> duruma göre şifre unuttum/değiştirme endpoint eklenebilir
#       -> yeni admin için register endpoint eklenebilir

# anasayfa içeriklerini admin güncelleyebilir

## login için onayı admin verecek
## LOGOUT -> refresh token bak (login içinde kullanılabilir)

##### PublicHolidayInitializer class ramazan ve kurban bayramlarını yıl bağlı service güncellemesi yapılacak

# GITHUB'A ATILACAK 13.06.2025'DEN ÖNCE

Dashbordlarda token ile giriş yapsınn token olmazsa dashbord gözükmesin

Admin onaylarsa şirketi şirket onaylansın endpoint sonra giriş yapabilsin.

Auth refresh token. Logout.  Veri tabanında refresh token sil. Veri tabnına kaydolması gerekiyor. Veri tabanında sakla o sakladığınla eşleştir tokenı

Activation kodunda id gerek yok verify mailde tokendan alabiliriz.

user deactivede role manager ise şirket kapanacak ise diğer o şirkette çalışan userlar da deactive olsun

//deactive abonelik süresi bitince olacak.
//user deactivede role manager ise kendini silemesin ise diğer o şirkette çalışan userlar da deactive olsun role bazlı kontrol
//manager employee silebilsin.
//admin şirketi silebilir abonelik dolunca otomatik silinebilir.

//companymapper kullan serviste.

//request header olacak token güvenlik için

//getler pageable olacak