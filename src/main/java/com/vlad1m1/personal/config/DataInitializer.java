package com.vlad1m1.personal.config;

import com.vlad1m1.personal.enums.NotificationSeverity;
import com.vlad1m1.personal.model.Alarm;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.model.RegionalNotification;
import com.vlad1m1.personal.repository.AlarmRepository;
import com.vlad1m1.personal.repository.CategoryRepository;
import com.vlad1m1.personal.repository.MemoRepository;
import com.vlad1m1.personal.repository.RegionRepository;
import com.vlad1m1.personal.repository.RegionalNotificationRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DataInitializer implements ApplicationRunner {
    private static final String FIRST_AID_CATEGORY = "Первая помощь";
    private static final String SAFETY_CATEGORY = "Безопасность";

    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;
    private final MemoRepository memoRepository;
    private final AlarmRepository alarmRepository;
    private final RegionalNotificationRepository notificationRepository;

    public DataInitializer(
            RegionRepository regionRepository,
            CategoryRepository categoryRepository,
            MemoRepository memoRepository,
            AlarmRepository alarmRepository,
            RegionalNotificationRepository notificationRepository
    ) {
        this.regionRepository = regionRepository;
        this.categoryRepository = categoryRepository;
        this.memoRepository = memoRepository;
        this.alarmRepository = alarmRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRegions();
        seedCategories();
        seedMemos();
        seedAlarms();
        seedNotifications();
    }

    private void seedRegions() {
        if (regionRepository.count() > 0) {
            return;
        }
        regionRepository.saveAll(List.of(
                region("Москва", "112"),
                region("Санкт-Петербург", "112"),
                region("Республика Татарстан", "112"),
                region("Краснодарский край", "112"),
                region("Свердловская область", "112"),
                region("Новосибирская область", "112")
        ));
    }

    private void seedCategories() {
        LocalDateTime now = LocalDateTime.now();
        findOrCreateCategory(FIRST_AID_CATEGORY, "medical_services", "#D32F2F", 1, now);
        findOrCreateCategory(SAFETY_CATEGORY, "health_and_safety", "#1E6FD4", 2, now);
    }

    private void seedMemos() {
        LocalDateTime now = LocalDateTime.now();
        Category firstAid = findOrCreateCategory(FIRST_AID_CATEGORY, "medical_services", "#D32F2F", 1, now);
        Category safety = findOrCreateCategory(SAFETY_CATEGORY, "health_and_safety", "#1E6FD4", 2, now);

        for (MemoSeed seed : memoSeeds()) {
            Category category = seed.safetyMemo() ? safety : firstAid;
            upsertMemo(category, seed, now);
        }
    }

    private List<MemoSeed> memoSeeds() {
        return List.of(
                new MemoSeed(
                        "Сердечно-легочная реанимация (СЛР)",
                        List.of(),
                        "Если человек не дышит или нет признаков жизни",
                        "favorite",
                        "#C62828",
                        List.of(
                                "Убедитесь, что место безопасно, громко обратитесь к человеку и проверьте реакцию.",
                                "Попросите кого-то вызвать 112 или позвоните сами на громкой связи.",
                                "Положите человека на спину на твердую поверхность и откройте дыхательные пути.",
                                "Делайте 30 сильных нажатий на центр грудной клетки с частотой 100-120 в минуту.",
                                "Продолжайте до приезда врачей или появления нормального дыхания."
                        ),
                        true,
                        "https://cdn.example.com/memos/cpr.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при кровотечении",
                        List.of("Кровотечение"),
                        "Как остановить наружное кровотечение",
                        "bloodtype",
                        "#B71C1C",
                        List.of(
                                "Защитите руки перчатками, пакетом или чистой тканью, если это возможно.",
                                "Прижмите чистую салфетку или бинт прямо к ране.",
                                "Держите давление 10-15 минут, не снимая пропитавшуюся повязку.",
                                "Приподнимите поврежденную конечность, если нет подозрения на перелом.",
                                "Если кровь идет сильно или не останавливается, вызывайте 112."
                        ),
                        true,
                        "https://cdn.example.com/memos/bleeding.png",
                        false
                ),
                new MemoSeed(
                        "Инородное тело в дыхательных путях",
                        List.of(),
                        "Помощь при удушье и сильном поперхивании",
                        "air",
                        "#1565C0",
                        List.of(
                                "Если человек может кашлять, попросите его продолжать кашлять.",
                                "Если он не может говорить или дышать, вызовите 112.",
                                "Сделайте до 5 ударов основанием ладони между лопатками.",
                                "Если не помогло, выполните до 5 толчков в живот выше пупка.",
                                "При потере сознания начните СЛР и ждите скорую помощь."
                        ),
                        true,
                        "https://cdn.example.com/memos/choking.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при потере сознания",
                        List.of("Потеря сознания"),
                        "Что делать при обмороке",
                        "medical_services",
                        "#6A1B9A",
                        List.of(
                                "Проверьте безопасность места и аккуратно уложите человека на спину.",
                                "Проверьте дыхание и вызовите 112, если сознание не возвращается быстро.",
                                "Если дыхание есть, поверните человека на бок.",
                                "Ослабьте тесную одежду и обеспечьте приток свежего воздуха.",
                                "Не давайте воду, еду или таблетки, пока человек полностью не пришел в себя."
                        ),
                        true,
                        "https://cdn.example.com/memos/unconscious.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при ожоге",
                        List.of("Ожоги"),
                        "Помощь при термическом ожоге",
                        "local_fire_department",
                        "#E65100",
                        List.of(
                                "Уберите источник ожога и снимите украшения рядом с поврежденным местом.",
                                "Охлаждайте ожог прохладной проточной водой 15-20 минут.",
                                "Не вскрывайте пузыри и не наносите масло, крем или спирт.",
                                "Накройте ожог чистой сухой повязкой или тканью.",
                                "Вызывайте 112 при большом ожоге, ожоге лица, дыхательных путей или у ребенка."
                        ),
                        true,
                        "https://cdn.example.com/memos/burn.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при переломе",
                        List.of("Переломы и ушибы"),
                        "Иммобилизация и покой до врача",
                        "healing",
                        "#2E7D32",
                        List.of(
                                "Не перемещайте пострадавшего без необходимости.",
                                "Зафиксируйте поврежденную часть в удобном положении.",
                                "Приложите холод через ткань на 10-15 минут.",
                                "При открытой ране накройте ее чистой повязкой.",
                                "Вызывайте 112 при сильной боли, деформации, открытом переломе или травме шеи."
                        ),
                        true,
                        "https://cdn.example.com/memos/fracture.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при вывихе или растяжении",
                        List.of(),
                        "Покой, холод и фиксация сустава",
                        "healing",
                        "#558B2F",
                        List.of(
                                "Остановите нагрузку и помогите человеку принять удобное положение.",
                                "Зафиксируйте сустав эластичным бинтом без сильного сдавливания.",
                                "Приложите холод через ткань на 10-15 минут.",
                                "Поднимите конечность, если это не усиливает боль.",
                                "Обратитесь к врачу при деформации, онемении или невозможности двигать суставом."
                        ),
                        false,
                        "https://cdn.example.com/memos/sprain.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при отравлении",
                        List.of("Отравление"),
                        "Пищевое, лекарственное или бытовое отравление",
                        "warning",
                        "#F9A825",
                        List.of(
                                "Вызовите 112 при отравлении лекарствами, химикатами, грибами или у ребенка.",
                                "Уберите источник отравления и проветрите помещение, если это безопасно.",
                                "Не вызывайте рвоту после кислот, щелочей, бензина и другой едкой химии.",
                                "Дайте воду маленькими глотками, только если человек в сознании.",
                                "Сохраните упаковку вещества или остатки еды для врачей."
                        ),
                        true,
                        "https://cdn.example.com/memos/poisoning.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при тепловом ударе",
                        List.of("Тепловой и солнечный удар"),
                        "Действия при перегреве организма",
                        "thermostat",
                        "#FF8F00",
                        List.of(
                                "Переместите человека в тень или прохладное место.",
                                "Снимите лишнюю одежду и обеспечьте приток воздуха.",
                                "Охлаждайте шею, лоб и подмышки влажной прохладной тканью.",
                                "Давайте прохладную воду маленькими глотками, если человек в сознании.",
                                "Вызывайте 112 при спутанности, судорогах, высокой температуре или потере сознания."
                        ),
                        true,
                        "https://cdn.example.com/memos/heatstroke.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при переохлаждении",
                        List.of("Обморожение"),
                        "Постепенное согревание при холоде",
                        "ac_unit",
                        "#0277BD",
                        List.of(
                                "Переместите человека в теплое сухое место.",
                                "Снимите влажную одежду и укройте пледом или курткой.",
                                "Согревайте постепенно, не растирайте кожу снегом, спиртом или руками.",
                                "Дайте теплое сладкое питье, если человек в сознании.",
                                "Вызывайте 112 при сонливости, спутанности, пузырях или почернении кожи."
                        ),
                        true,
                        "https://cdn.example.com/memos/hypothermia.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при укусе животного",
                        List.of(),
                        "Снижение риска инфекции после укуса",
                        "pets",
                        "#6D4C41",
                        List.of(
                                "Отойдите в безопасное место и не пытайтесь ловить животное.",
                                "Промывайте рану водой с мылом 10-15 минут.",
                                "Остановите кровотечение чистой повязкой.",
                                "Накройте рану стерильной салфеткой или чистой тканью.",
                                "Обратитесь к врачу в тот же день для обработки и решения о прививках."
                        ),
                        false,
                        "https://cdn.example.com/memos/animal-bite.png",
                        false
                ),
                new MemoSeed(
                        "Первая помощь при ударе током",
                        List.of("Удар током"),
                        "Безопасность и помощь после электротравмы",
                        "flash_on",
                        "#F57F17",
                        List.of(
                                "Не касайтесь пострадавшего, пока источник тока не отключен.",
                                "Отключите питание или отодвиньте провод сухим непроводящим предметом.",
                                "Проверьте дыхание и сознание, при необходимости начните СЛР.",
                                "Накройте ожоги сухой чистой повязкой.",
                                "Вызывайте 112 после любой электротравмы, особенно при боли в груди или ожогах."
                        ),
                        true,
                        "https://cdn.example.com/memos/electric-shock.png",
                        false
                ),
                new MemoSeed(
                        "Действия при пожаре",
                        List.of("Пожар и эвакуация"),
                        "Как быстро и безопасно эвакуироваться",
                        "local_fire_department",
                        "#D84315",
                        List.of(
                                "Позвоните 101 или 112 и назовите адрес пожара.",
                                "Покиньте помещение по лестнице, не пользуйтесь лифтом.",
                                "При дыме двигайтесь ниже к полу и прикрывайте рот влажной тканью.",
                                "Закрывайте за собой двери, чтобы замедлить распространение дыма.",
                                "Если выход отрезан, выйдите на балкон или к окну и подайте сигнал спасателям."
                        ),
                        true,
                        "https://cdn.example.com/memos/fire.png",
                        true
                ),
                new MemoSeed(
                        "Действия при угрозе наводнения",
                        List.of(),
                        "Что сделать при подъеме воды",
                        "flood",
                        "#0277BD",
                        List.of(
                                "Следите за сообщениями экстренных служб и подготовьте документы.",
                                "Отключите электричество, газ и воду, если есть время и это безопасно.",
                                "Перенесите ценные вещи и аптечку на верхний этаж или выше уровня пола.",
                                "Перейдите на возвышенное место или к пункту эвакуации.",
                                "Не заходите в поток воды и не проезжайте затопленные участки."
                        ),
                        true,
                        "https://cdn.example.com/memos/flood.png",
                        true
                ),
                new MemoSeed(
                        "Действия при сильном ветре",
                        List.of(),
                        "Как снизить риск травм при шторме",
                        "air",
                        "#00838F",
                        List.of(
                                "Уберите с балкона и двора предметы, которые может унести ветер.",
                                "Закройте окна и держитесь подальше от стекол.",
                                "Оставайтесь в помещении, если нет срочной необходимости выходить.",
                                "На улице обходите деревья, рекламные щиты и линии электропередачи.",
                                "При обрыве проводов или травмах звоните 112."
                        ),
                        true,
                        "https://cdn.example.com/memos/strong-wind.png",
                        true
                ),
                new MemoSeed(
                        "Действия при землетрясении",
                        List.of(),
                        "Безопасное поведение во время толчков",
                        "crisis_alert",
                        "#5D4037",
                        List.of(
                                "Во время толчков присядьте, укройтесь под прочной мебелью и держитесь за нее.",
                                "Держитесь подальше от окон, шкафов и тяжелых предметов.",
                                "Не пользуйтесь лифтом и не выбегайте на лестницу во время сильных толчков.",
                                "После толчков отключите газ и электричество, если это безопасно.",
                                "Выйдите на открытое место и ждите сообщений экстренных служб."
                        ),
                        true,
                        "https://cdn.example.com/memos/earthquake.png",
                        true
                ),
                new MemoSeed(
                        "Действия при утечке газа",
                        List.of(),
                        "Что делать при запахе газа",
                        "gas_meter",
                        "#455A64",
                        List.of(
                                "Не включайте свет, электроприборы и не пользуйтесь открытым огнем.",
                                "Перекройте газовый кран, если можете сделать это безопасно.",
                                "Откройте окна и двери для проветривания.",
                                "Выведите людей из помещения и покиньте здание.",
                                "Позвоните 104 или 112 с улицы или из соседнего безопасного места."
                        ),
                        true,
                        "https://cdn.example.com/memos/gas-leak.png",
                        true
                )
        );
    }

    private void upsertMemo(Category category, MemoSeed seed, LocalDateTime now) {
        List<Memo> existingMemos = memoRepository.findByTitleIn(seed.lookupTitles());
        Memo memo = existingMemos.stream()
                .filter(existingMemo -> Objects.equals(existingMemo.getTitle(), seed.title()))
                .findFirst()
                .or(() -> existingMemos.stream().findFirst())
                .orElseGet(Memo::new);

        boolean changed = memo.getId() == null;
        if (memo.getCreatedAt() == null) {
            memo.setCreatedAt(now);
            changed = true;
        }
        if (memo.getUpdatedAt() == null) {
            changed = true;
        }
        if (memo.getCategory() == null || !Objects.equals(memo.getCategory().getId(), category.getId())) {
            memo.setCategory(category);
            changed = true;
        }
        if (memo.getRegion() != null) {
            memo.setRegion(null);
            changed = true;
        }
        changed |= setStringIfChanged(memo.getTitle(), seed.title(), memo::setTitle);
        changed |= setStringIfChanged(memo.getShortDescription(), seed.shortDescription(), memo::setShortDescription);
        changed |= setStringIfChanged(memo.getContent(), htmlContent(seed), memo::setContent);
        changed |= setStringIfChanged(memo.getIconName(), seed.iconName(), memo::setIconName);
        changed |= setStringIfChanged(memo.getAccentColor(), seed.accentColor(), memo::setAccentColor);
        changed |= setStringIfChanged(memo.getImageUrl(), seed.imageUrl(), memo::setImageUrl);
        if (!Objects.equals(memo.getSteps(), seed.steps())) {
            memo.setSteps(new ArrayList<>(seed.steps()));
            changed = true;
        }
        if (memo.getVersion() != 1) {
            memo.setVersion(1);
            changed = true;
        }
        if (memo.isCritical() != seed.critical()) {
            memo.setCritical(seed.critical());
            changed = true;
        }
        if (memo.isActive() != seed.active()) {
            memo.setActive(seed.active());
            changed = true;
        }

        if (changed) {
            memo.setUpdatedAt(now);
            memoRepository.save(memo);
        }
    }

    private boolean setStringIfChanged(String current, String next, java.util.function.Consumer<String> setter) {
        if (Objects.equals(current, next)) {
            return false;
        }
        setter.accept(next);
        return true;
    }

    private String htmlContent(MemoSeed seed) {
        return "<h1>" + seed.title() + "</h1><p>" + seed.shortDescription() + "</p>";
    }

    private void seedAlarms() {
        if (alarmRepository.count() > 0) {
            return;
        }
        List<Region> regions = regionRepository.findAll();
        if (regions.isEmpty()) {
            return;
        }
        List<String> texts = List.of(
                "В вашем регионе объявлено экстренное уведомление. Сохраняйте спокойствие, проверяйте официальные источники информации и следуйте инструкциям местных служб. Не распространяйте неподтвержденные сообщения и не выезжайте в зону риска без необходимости.",
                "Обнаружена потенциально опасная ситуация в выбранном регионе. Избегайте поездок в зону риска, предупредите близких, держите телефон заряженным и ожидайте дальнейших указаний от экстренных служб.",
                "Поступило важное региональное предупреждение. Проверьте актуальные памятки, подготовьте документы, запас воды, лекарства и средства связи. При ухудшении ситуации обращайтесь по номеру 112.",
                "Для вашего региона опубликовано срочное уведомление. Ориентируйтесь только на сообщения официальных служб, не подходите к опасным участкам и заранее продумайте безопасный маршрут эвакуации."
        );
        for (int index = 0; index < Math.min(regions.size(), texts.size()); index++) {
            alarmRepository.save(alarm(regions.get(index), texts.get(index)));
        }
    }

    private void seedNotifications() {
        if (notificationRepository.count() > 0) {
            return;
        }
        List<Region> regions = regionRepository.findAll();
        if (regions.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        for (Region region : regions) {
            notificationRepository.save(notification(region, "Погодное предупреждение", "Следите за сообщениями экстренных служб и избегайте рискованных маршрутов.", NotificationSeverity.WARNING, now.minusHours(2)));
        }
        regions.stream().filter(region -> region.getRegionName().equals("Москва")).findFirst()
                .ifPresent(region -> notificationRepository.save(notification(region, "Штормовой ветер", "Ожидается сильный дождь, гроза и порывы ветра до 20 м/с.", NotificationSeverity.DANGER, now.minusMinutes(45))));
        regions.stream().filter(region -> region.getRegionName().equals("Краснодарский край")).findFirst()
                .ifPresent(region -> notificationRepository.save(notification(region, "Экстренное предупреждение", "Возможны смерчи над морем на участке Анапа - Магри.", NotificationSeverity.CRITICAL, now.minusMinutes(20))));
    }

    private Category findOrCreateCategory(String name, String iconName, String accentColor, int displayOrder, LocalDateTime updatedAt) {
        return categoryRepository.findAll().stream()
                .filter(category -> Objects.equals(category.getName(), name))
                .findFirst()
                .orElseGet(() -> categoryRepository.save(category(name, iconName, accentColor, displayOrder, updatedAt)));
    }

    private Region region(String name, String emergencyPhone) {
        Region region = new Region();
        region.setRegionName(name);
        region.setEmergencyPhone(emergencyPhone);
        return region;
    }

    private Category category(String name, String iconName, String accentColor, int displayOrder, LocalDateTime updatedAt) {
        Category category = new Category();
        category.setName(name);
        category.setIconName(iconName);
        category.setAccentColor(accentColor);
        category.setDisplayOrder(displayOrder);
        category.setUpdatedAt(updatedAt);
        return category;
    }

    private RegionalNotification notification(Region region, String title, String message, NotificationSeverity severity, LocalDateTime publishedAt) {
        RegionalNotification notification = new RegionalNotification();
        notification.setRegion(region);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setSeverity(severity);
        notification.setPublishedAt(publishedAt);
        notification.setActive(true);
        return notification;
    }

    private Alarm alarm(Region region, String text) {
        Alarm alarm = new Alarm();
        alarm.setRegion(region);
        alarm.setText(text);
        return alarm;
    }

    private record MemoSeed(
            String title,
            List<String> aliases,
            String shortDescription,
            String iconName,
            String accentColor,
            List<String> steps,
            boolean critical,
            String imageUrl,
            boolean safetyMemo
    ) {
        private List<String> lookupTitles() {
            List<String> titles = new ArrayList<>();
            titles.add(title);
            titles.addAll(aliases);
            return titles;
        }

        private boolean active() {
            return true;
        }
    }
}
