# SE311 HR Application - Design Pattern Notes

Bu dosya teslim icin degil, projeyi anlamak ve sunumda savunmak icin yerel not olarak hazirlanmistir. Kodun genel amaci, insan kaynaklari departmaninin sirket organizasyon semasini yonetmesini modellemektir. Sistem departmanlari, takimlari ve calisanlari hiyerarsik olarak tutar; ise alma, isten cikarma, terfi, departman birlestirme ve ayirma gibi HR operasyonlarini uygular; ust yonetime bildirim gonderir ve haftalik raporlar uretir.

Projede kullanilan ana pattern'ler:

- Composite
- Factory
- Singleton
- Command
- Observer
- Visitor

Bu pattern'ler izole degil, ayni is akisi icinde birbirini tamamlar. Main sinifi sadece demo senaryosunu kurar; gercek modelleme isi model, command, visitor, observer, factory ve singleton paketlerine dagitilmistir.

---

## Genel Akis

1. Main sinifi once `OrgChartManager` singleton nesnesini alir.
2. `CorporateHead` olusturulur ve manager uzerinden organizasyon degisikliklerini dinleyecek kisi olarak atanir.
3. `HRFactory` ile departman, takim ve calisan nesneleri yaratilir.
4. `Department`, `Team` ve `Employee` nesneleri Composite yapisi ile organizasyon agacina yerlestirilir.
5. HR operasyonlari dogrudan metod cagrisi olarak degil, Command nesneleri olarak calistirilir.
6. Merge ve split operasyonlarinda Department observer'lara bildirim gonderir.
7. Raporlar Visitor nesneleri ile organizasyon agaci gezilerek uretilir.
8. Calistirilan command'ler CommandHistory icinde audit log olarak saklanir.

---

## 1. Composite Pattern

### Neden Kullanildi?

Senaryoda sirket organizasyonu dogal olarak agac yapisindadir:

- Department en ust organizasyon birimidir.
- Team departman altindaki ara birimdir.
- Employee yaprak dugumdur.

Bu nedenle Composite pattern uygundur. Cunku HR sistemi bazen tek bir calisani, bazen bir takimi, bazen de butun departmani ayni organizasyon semasinin parcasi olarak ele alir.

### Koddaki Yeri

- `model/OrgComponent.java`
- `model/Department.java`
- `model/Team.java`
- `model/Employee.java`

### Participant Listing

- Component: `OrgComponent`
- Composite: `Department`, `Team`
- Leaf: `Employee`
- Client: `Main`, `OrgChartManager`, `Visitor` siniflari, `Command` siniflari

### Siniflarin Gorevleri

`OrgComponent`, organizasyondaki tum elemanlarin ortak arayuzudur. `getName`, `accept` ve `printDetails` metodlarini tanimlar. Bu sayede departman, takim ve calisan nesneleri ortak tip uzerinden kullanilabilir.

`Department`, ust seviye composite nesnedir. Icinde `OrgComponent` listesi tutar. Bu liste teorik olarak takim veya baska organizasyon elemanlarini barindirabilir. `addChild`, `removeChild`, `getChildren`, `merge`, `split`, `accept` ve `printDetails` metodlari ile hem agac yapisini hem de departman islemlerini yonetir.

`Team`, ara composite nesnedir. Icinde calisanlari veya genel olarak `OrgComponent` nesnelerini tutar. `addMember`, `removeMember`, `getMembers`, `accept` ve `printDetails` metodlari vardir.

`Employee`, leaf nesnedir. Alt eleman barindirmaz. Calisanin adi, cinsiyeti, ise giris tarihi, maasi, unvani ve manager olup olmadigi gibi bilgileri tutar.

### Senaryoyla Iliskisi

Organizasyon semasini yazdirmak, raporlamak veya degistirmek gerektiginde sistem agaci gezer. Visitor pattern de bu Composite yapinin uzerinde calisir. Command siniflari da hedef olarak bu yapidaki belirli nesneleri kullanir; mesela `HireEmployeeCommand` bir `Team` icine `Employee` ekler, `MergeDepartmentCommand` bir `Department` icindeki takimlari baska departmana tasir.

---

## 2. Factory Pattern

### Neden Kullanildi?

Main sinifinin dogrudan `new Department`, `new Team`, `new Employee` gibi nesne yaratma detaylarina baglanmamasi icin Factory kullanilmistir. Bu pattern, organizasyon elemanlarinin olusturulmasini tek bir olusturma arayuzunde toplar.

### Koddaki Yeri

- `factory/OrgComponentFactory.java`
- `factory/HRFactory.java`

### Participant Listing

- Creator / Factory Interface: `OrgComponentFactory`
- Concrete Creator / Concrete Factory: `HRFactory`
- Product Types: `Department`, `Team`, `Employee`
- Client: `Main`

### Siniflarin Gorevleri

`OrgComponentFactory`, calisan, manager, takim ve departman yaratmak icin ortak metodlari tanimlar.

`HRFactory`, bu arayuzu uygular. `createEmployee` normal calisan yaratir. `createManager` manager unvanli ve `isManager = true` olan bir calisan yaratir. `createTeam` takim, `createDepartment` departman yaratir.

### Senaryoyla Iliskisi

HR uygulamasinda yeni calisan ise alinabilir, manager yaratilmaya ihtiyac olabilir veya yeni departman/takim kurulabilir. Factory bu olusturma islemlerini tek noktada toplar. Projede tek concrete factory oldugu icin pattern'in etkisi cok buyuk degil, ama yaratma sorumlulugunu ayirdigi icin mimari olarak savunulabilir.

---

## 3. Singleton Pattern

### Neden Kullanildi?

Organizasyon semasi uygulamada tek bir merkezi kaynaktan yonetilmelidir. Birden fazla `OrgChartManager` olursa farkli departman listeleri, farkli command history'leri ve tutarsiz raporlar olusabilir. Singleton bu riski azaltmak icin kullanilmistir.

### Koddaki Yeri

- `singleton/OrgChartManager.java`

### Participant Listing

- Singleton: `OrgChartManager`
- Client: `Main`, `SplitDepartmentCommand`, rapor ve command akisi

### Sinifin Gorevi

`OrgChartManager`, organizasyon semasinin merkezi yoneticisidir. Departman listesini tutar, corporate head observer'ini mevcut departmanlara ekler, command calistirir, command history'yi yonetir, rapor uretir ve organizasyon semasini yazdirir.

`getInstance` metodu tek instance yaratir ve ayni instance'i geri dondurur. Constructor private oldugu icin disaridan dogrudan nesne yaratilamaz.

### Senaryoyla Iliskisi

Senaryoda HR departmani organizasyon semasini gunceller. Bu semanin tek merkezden yonetilmesi mantiklidir. `OrgChartManager`, Command, Observer ve Visitor pattern'lerinin baglandigi merkezdir:

- Command'leri calistirir.
- CommandHistory ile audit log tutar.
- Visitor raporlarini tum departmanlar uzerinde calistirir.
- CorporateHead'i departmanlara observer olarak baglar.

---

## 4. Command Pattern

### Neden Kullanildi?

Senaryoda HR departmanina cesitli istekler gelir:

- Yeni calisan ise alinir.
- Calisan terfi ettirilir.
- Calisan isten cikarilir.
- Departmanlar birlestirilir.
- Departman tekrar ayrilir.

Bu islemler request olarak modellenmeye uygundur. Command pattern sayesinde her operasyon ayri bir nesne olur. Bu da undo, audit log ve operasyonlarin ortak sekilde calistirilmesini kolaylastirir.

### Koddaki Yeri

- `command/HRCommand.java`
- `command/HireEmployeeCommand.java`
- `command/LayOffEmployeeCommand.java`
- `command/PromoteEmployeeCommand.java`
- `command/MergeDepartmentCommand.java`
- `command/SplitDepartmentCommand.java`
- `command/CommandHistory.java`

### Participant Listing

- Command Interface: `HRCommand`
- Concrete Commands:
  - `HireEmployeeCommand`
  - `LayOffEmployeeCommand`
  - `PromoteEmployeeCommand`
  - `MergeDepartmentCommand`
  - `SplitDepartmentCommand`
- Invoker: `OrgChartManager`
- Receiver:
  - `Team` for hire and layoff
  - `Employee` for promote
  - `Department` for merge and split
  - `OrgChartManager` also participates in split because new department is added to the root list
- History / Audit Support: `CommandHistory`
- Client: `Main`

### Siniflarin Gorevleri

`HRCommand`, tum HR operasyonlari icin ortak arayuzdur. Her command `execute`, `undo` ve `getDescription` metodlarini uygular.

`HireEmployeeCommand`, bir calisani hedef takima ekler. Undo yapildiginda ayni calisan takimdan cikarilir.

`LayOffEmployeeCommand`, bir calisani hedef takimdan cikarir. Undo yapildiginda calisan tekrar takima eklenir.

`PromoteEmployeeCommand`, calisanin unvanini ve maasini degistirir, manager flag'ini true yapar. Undo yapildiginda eski unvan ve eski maas geri yuklenir.

`MergeDepartmentCommand`, source departmandaki cocuklari target departmana tasir. Undo icin source departmanin eski cocuklarini saklar.

`SplitDepartmentCommand`, source departmandan belirli takimlari ayirarak yeni departman yaratir ve bunu `OrgChartManager` departman listesine ekler. Undo yapildiginda yeni departman kaldirilir ve cocuklari source departmana geri eklenir.

`CommandHistory`, calistirilan command'leri history listesinde ve audit log listesinde saklar. `push` metodu command'i kaydeder ve zaman damgali log uretir. `pop` metodu undo icin son command'i dondurur.

### Senaryoyla Iliskisi

Proje metni yapisal degisikliklerin validasyon ve audit icin loglanmasi gerektigini soyler. Command pattern bu ihtiyaca dogrudan cevap verir. Her operasyonun acik bir command nesnesi vardir; `OrgChartManager.executeCommand` bu command'i calistirir ve sonra `CommandHistory` icine kaydeder.

Bu pattern ayrica undo mantigini da destekler. Ozellikle "previously merged department splits back again" ifadesi, onceki duruma donme fikrine yakindir. Bu yuzden Command pattern senaryoya cok uygundur.

### Dikkat Edilmesi Gereken Nokta

Bu kodda split islemi genel bir "takimlari ayir ve yeni departman olustur" islemi olarak tasarlanmistir. Senaryodaki "as if it never merged" cumlesini daha guclu savunmak icin split'in merge gecmisine dayali calistigi aciklanmalidir veya ileride kodda merge gecmisi daha net tutulmalidir.

---

## 5. Observer Pattern

### Neden Kullanildi?

Senaryoda departmanlar merge veya split oldugunda corporate head'in notification almasi istenir. Bu tam olarak Observer pattern'in kullanim alanidir: bir subject'te olay olur, ona bagli observer'lar otomatik bilgilendirilir.

### Koddaki Yeri

- `observer/OrgSubject.java`
- `observer/OrgObserver.java`
- `observer/CorporateHead.java`
- `model/Department.java`
- `singleton/OrgChartManager.java`

### Participant Listing

- Subject Interface: `OrgSubject`
- Concrete Subject: `Department`
- Observer Interface: `OrgObserver`
- Concrete Observer: `CorporateHead`
- Observer Registration Coordinator: `OrgChartManager`
- Client: `Main`

### Siniflarin Gorevleri

`OrgSubject`, observer ekleme, observer cikarma ve notification gonderme metodlarini tanimlar.

`OrgObserver`, notification alacak siniflarin uyguladigi arayuzdur. `update` metodu vardir.

`CorporateHead`, concrete observer'dir. `update` metodu cagrildiginda bildirimi ekrana yazar.

`Department`, concrete subject olarak observer listesini tutar. `merge` ve `split` islemlerinin sonunda `notifyObservers` cagrilir.

`OrgChartManager`, corporate head atandiginda mevcut departmanlara observer olarak ekler. Yeni departman eklendiginde de corporate head varsa ona baglar.

### Senaryoyla Iliskisi

Departman birlestirme ve ayirma olaylari merkezi olarak `Department` icinde gerceklesir. Bu olaylardan sonra CorporateHead otomatik bilgilendirilir. Main sinifi notification'i manuel cagirmadigi icin Observer pattern dogru sekilde kullanilmis olur.

---

## 6. Visitor Pattern

### Neden Kullanildi?

Senaryoda corporate head haftalik raporlar ister:

- Diversity report
- Seniority report
- Iki ek rapor

Bu raporlar organizasyon agacini gezerek calisan bilgilerini toplar. Model siniflarina her yeni rapor icin yeni metod eklemek yerine Visitor pattern kullanilmistir. Boylece yeni rapor eklemek icin yeni bir visitor sinifi yazmak yeterlidir.

### Koddaki Yeri

- `visitor/ReportVisitor.java`
- `visitor/DiversityReportVisitor.java`
- `visitor/SeniorityReportVisitor.java`
- `visitor/SalaryBandReportVisitor.java`
- `visitor/HeadcountReportVisitor.java`
- `model/Department.java`
- `model/Team.java`
- `model/Employee.java`
- `singleton/OrgChartManager.java`

### Participant Listing

- Visitor Interface: `ReportVisitor`
- Concrete Visitors:
  - `DiversityReportVisitor`
  - `SeniorityReportVisitor`
  - `SalaryBandReportVisitor`
  - `HeadcountReportVisitor`
- Element Interface: `OrgComponent`
- Concrete Elements:
  - `Department`
  - `Team`
  - `Employee`
- Object Structure: Composite organization tree
- Client: `OrgChartManager.generateReport`, `Main`

### Siniflarin Gorevleri

`ReportVisitor`, her organizasyon elemani tipi icin ayri visit metodu tanimlar: `visitEmployee`, `visitTeam`, `visitDepartment`. Ayrica rapor sonucunu yazdirmak icin `printReport` vardir.

`DiversityReportVisitor`, employee dugumlerini ziyaret ederek male/female sayilarini toplar ve oranlari yazdirir.

`SeniorityReportVisitor`, calisanin sirket yilini kontrol eder ve 20 yil veya daha fazla calisanlari listeler.

`SalaryBandReportVisitor`, calisanlari maas araliklarina gore gruplar ve ortalama maasi hesaplar.

`HeadcountReportVisitor`, departman bazinda calisan sayisini hesaplar. `visitDepartment` ile aktif departmani set eder, `visitEmployee` ile o departmanin sayacini artirir.

`Department.accept`, once visitor'in `visitDepartment` metodunu cagirir, sonra cocuklarini gezer. `Team.accept`, once `visitTeam` cagirir, sonra uyelerini gezer. `Employee.accept`, sadece `visitEmployee` cagirir.

### Senaryoyla Iliskisi

Raporlama ihtiyaci Visitor pattern icin dogal bir gerekcedir. Yeni rapor eklendiginde `Department`, `Team` ve `Employee` siniflarina yeni rapor metodlari eklenmez. Bu, Open/Closed Principle acisindan savunulabilir: model siniflari degismeden yeni visitor eklenebilir.

---

## Pattern'lerin Birbirine Baglanmasi

Bu projede pattern'ler ayri ayri gostermek icin konmamistir; ayni akis icinde birlikte calisir:

- Factory, Composite yapida kullanilacak nesneleri olusturur.
- Composite, organizasyon semasinin veri yapisini kurar.
- Singleton, bu organizasyon semasinin tek merkezden yonetilmesini saglar.
- Command, Composite uzerinde HR degisiklikleri yapar.
- CommandHistory, Command'leri audit log ve undo icin saklar.
- Observer, Command tarafindan tetiklenen merge/split degisikliklerinde CorporateHead'i bilgilendirir.
- Visitor, Singleton manager'in tuttugu Composite agac uzerinde rapor uretir.

Ornek akis:

1. `Main`, `HRFactory` ile `Department`, `Team`, `Employee` yaratir.
2. Bu nesneler Composite agacina eklenir.
3. `OrgChartManager.executeCommand(new MergeDepartmentCommand(...))` cagrilir.
4. Command, `Department.merge` metodunu calistirir.
5. `Department.merge`, observer'lara notification yollar.
6. `OrgChartManager`, command'i `CommandHistory` icine audit log olarak kaydeder.
7. Daha sonra `OrgChartManager.generateReport(new DiversityReportVisitor())` ile ayni organizasyon agaci rapor icin gezilir.

Bu akis pattern'lerin baglantili oldugunu gosterir.

---

## Sunumda Savunulabilecek Ana Cumleler

Composite icin: "Organizasyon yapisi agac oldugu icin Department, Team ve Employee nesnelerini ortak OrgComponent arayuzu altinda modelledik."

Visitor icin: "Raporlar organizasyon agacini geziyor. Yeni rapor eklemek istedigimizde model siniflarini degistirmek yerine yeni visitor sinifi ekliyoruz."

Command icin: "HR taleplerini nesne olarak modelledik. Bu sayede execute, undo ve audit log davranislarini standart hale getirdik."

Observer icin: "Corporate head merge/split olaylarina dogrudan bagimli degil. Department subject olarak notification yayinliyor, CorporateHead observer olarak dinliyor."

Singleton icin: "Organizasyon semasinin tek tutarli kaynaktan yonetilmesi icin OrgChartManager'i singleton yaptik."

Factory icin: "Organizasyon elemanlarinin olusturulmasini client kodundan ayirdik ve HRFactory altinda topladik."

---

## Tasarimla Ilgili Kritik Degerlendirme

Genel tasarim pattern dersi icin uygundur. En guclu pattern'ler Composite, Command, Visitor ve Observer'dir. Bunlar senaryodaki ana ihtiyaclara dogrudan karsilik gelir.

Factory pattern daha basit kalmistir cunku tek concrete factory vardir. Yine de nesne yaratma sorumlulugunu ayirdigi icin kabul edilebilir.

Singleton pattern akademik proje icin savunulabilir; ancak gercek buyuk sistemlerde test edilebilirlik ve global state nedeniyle dikkatli kullanilmasi gerekir. Bu projede organizasyon chart'inin tek merkezden yonetilmesi gerekcesiyle mantiklidir.

Merge/split davranisi sunumda dikkatli anlatilmalidir. Kodda split genel bir departman ayirma islemi gibi calisir. Proje metnindeki "as if it never merged" ifadesi daha katidir; ideal tasarimda merge gecmisi daha acik saklanip split sadece onceki merge'i geri alacak sekilde validate edilebilir.

---

## Paket Bazli Kod Ozeti

### main

`Main.java`, demo senaryosunu calistirir. Pattern'leri tek tek gostermek icin nesneleri yaratir, organizasyon agacini kurar, command'leri calistirir, undo gosterir ve raporlar uretir.

### model

Organizasyonun temel domain modelidir. `OrgComponent`, `Department`, `Team`, `Employee` burada yer alir. Composite ve Visitor entegrasyonunun merkezi bu pakettir.

### factory

Organizasyon nesnelerini yaratir. `OrgComponentFactory` arayuz, `HRFactory` concrete factory'dir.

### singleton

`OrgChartManager`, organizasyon semasini merkezi olarak tutar. Departman ekleme/cikarma, command calistirma, undo, report generation ve audit log yazdirma burada yapilir.

### command

HR operasyonlarini temsil eder. Her operasyon kendi sinifina ayrilmistir. `CommandHistory`, audit log ve undo icin command gecmisini tutar.

### observer

Corporate head notification yapisini modeller. `Department`, subject olarak olaylari yayinlar. `CorporateHead`, observer olarak olaylari alir.

### visitor

Raporlari modeller. Her rapor ayri visitor sinifidir ve Composite agaci uzerindeki elemanlari ziyaret ederek sonuc uretir.

