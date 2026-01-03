package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.HardwareComponentMapper;
import cn.gcc.gearcorelab.model.HardwareComponent;
import cn.gcc.gearcorelab.service.HardwareComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class HardwareComponentServiceImpl implements HardwareComponentService {
    
    @Autowired
    private HardwareComponentMapper hardwareComponentMapper;
    
    @Override
    public List<HardwareComponent> getComponentsByType(String type) {
        return hardwareComponentMapper.findByType(type);
    }
    
    @Override
    public List<HardwareComponent> getAllComponents() {
        return hardwareComponentMapper.findAll();
    }
    
    @Override
    public HardwareComponent getComponentById(Long id) {
        return hardwareComponentMapper.findById(id);
    }
    
    @Override
    public boolean addComponent(HardwareComponent component) {
        component.setCreatedAt(LocalDateTime.now());
        component.setUpdatedAt(LocalDateTime.now());
        return hardwareComponentMapper.insert(component) > 0;
    }
    
    @Override
    public boolean updateComponent(HardwareComponent component) {
        component.setUpdatedAt(LocalDateTime.now());
        return hardwareComponentMapper.update(component) > 0;
    }
    
    @Override
    public boolean deleteComponent(Long id) {
        return hardwareComponentMapper.deleteById(id) > 0;
    }
    
    @Override
    public List<HardwareComponent> searchComponents(String keyword) {
        return hardwareComponentMapper.searchByKeyword(keyword);
    }
    
    @Override
    public Map<String, Integer> getComponentStatistics() {
        List<Map<String, Object>> stats = hardwareComponentMapper.getTypeStatistics();
        Map<String, Integer> result = new HashMap<>();
        
        // 初始化所有类型为0
        String[] types = {"cpu", "motherboard", "ram", "gpu", "storage", "psu", "case"};
        for (String type : types) {
            result.put(type, 0);
        }
        
        // 填充实际统计数据
        for (Map<String, Object> stat : stats) {
            String type = (String) stat.get("type");
            Integer count = ((Number) stat.get("count")).intValue();
            result.put(type, count);
        }
        
        return result;
    }
    
    @Override
    public void initializeDefaultData() {
        // 检查是否已有数据
        List<HardwareComponent> existingComponents = getAllComponents();
        if (!existingComponents.isEmpty()) {
            return; // 已有数据，不需要初始化
        }
        
        // 初始化默认CPU数据
        initializeCPUData();
        
        // 初始化默认主板数据
        initializeMotherboardData();
        
        // 初始化默认内存数据
        initializeRAMData();
        
        // 初始化默认显卡数据
        initializeGPUData();
        
        // 初始化默认存储数据
        initializeStorageData();
        
        // 初始化默认电源数据
        initializePSUData();
        
        // 初始化默认机箱数据
        initializeCaseData();
    }
    
    @Override
    public boolean batchImportComponents(List<HardwareComponent> components) {
        try {
            for (HardwareComponent component : components) {
                addComponent(component);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void initializeCPUData() {
        HardwareComponent cpu1 = new HardwareComponent();
        cpu1.setName("AMD Ryzen 7 7700X");
        cpu1.setBrand("AMD");
        cpu1.setType("cpu");
        cpu1.setCategory("Ryzen 7000");
        cpu1.setSpecifications("{\"generation\":\"Zen 4\",\"socket\":\"AM5\",\"cores\":8,\"threads\":16,\"baseClock\":\"4.5GHz\",\"boostClock\":\"5.4GHz\"}");
        cpu1.setPrice(new BigDecimal("2299"));
        addComponent(cpu1);
        
        HardwareComponent cpu2 = new HardwareComponent();
        cpu2.setName("Intel Core i7-13700K");
        cpu2.setBrand("Intel");
        cpu2.setType("cpu");
        cpu2.setCategory("13th Gen");
        cpu2.setSpecifications("{\"generation\":\"Raptor Lake\",\"socket\":\"LGA1700\",\"cores\":16,\"threads\":24,\"baseClock\":\"3.4GHz\",\"boostClock\":\"5.4GHz\"}");
        cpu2.setPrice(new BigDecimal("2599"));
        addComponent(cpu2);
    }
    
    private void initializeMotherboardData() {
        HardwareComponent mb1 = new HardwareComponent();
        mb1.setName("MSI MAG B650 TOMAHAWK WIFI");
        mb1.setBrand("MSI");
        mb1.setType("motherboard");
        mb1.setCategory("B650");
        mb1.setSpecifications("{\"chipset\":\"B650\",\"socket\":\"AM5\",\"memoryType\":\"DDR5\",\"maxMemory\":\"128GB\",\"memorySlots\":4}");
        mb1.setPrice(new BigDecimal("1299"));
        addComponent(mb1);
        
        HardwareComponent mb2 = new HardwareComponent();
        mb2.setName("ASUS ROG STRIX Z790-E GAMING WIFI");
        mb2.setBrand("ASUS");
        mb2.setType("motherboard");
        mb2.setCategory("Z790");
        mb2.setSpecifications("{\"chipset\":\"Z790\",\"socket\":\"LGA1700\",\"memoryType\":\"DDR5\",\"maxMemory\":\"128GB\",\"memorySlots\":4}");
        mb2.setPrice(new BigDecimal("2899"));
        addComponent(mb2);
    }
    
    private void initializeRAMData() {
        HardwareComponent ram1 = new HardwareComponent();
        ram1.setName("G.SKILL Trident Z5 RGB 32GB");
        ram1.setBrand("G.SKILL");
        ram1.setType("ram");
        ram1.setCategory("DDR5");
        ram1.setSpecifications("{\"type\":\"DDR5\",\"speed\":6000,\"capacity\":32,\"modules\":2,\"timing\":\"CL36\"}");
        ram1.setPrice(new BigDecimal("1199"));
        addComponent(ram1);
        
        HardwareComponent ram2 = new HardwareComponent();
        ram2.setName("海盗船 Vengeance LPX 16GB");
        ram2.setBrand("海盗船");
        ram2.setType("ram");
        ram2.setCategory("DDR4");
        ram2.setSpecifications("{\"type\":\"DDR4\",\"speed\":3200,\"capacity\":16,\"modules\":2,\"timing\":\"CL16\"}");
        ram2.setPrice(new BigDecimal("399"));
        addComponent(ram2);
    }
    
    private void initializeGPUData() {
        HardwareComponent gpu1 = new HardwareComponent();
        gpu1.setName("NVIDIA GeForce RTX 4070 Ti");
        gpu1.setBrand("NVIDIA");
        gpu1.setType("gpu");
        gpu1.setCategory("RTX 40系");
        gpu1.setSpecifications("{\"series\":\"RTX 4070 Ti\",\"memory\":12,\"memoryType\":\"GDDR6X\",\"powerConsumption\":285}");
        gpu1.setPrice(new BigDecimal("4999"));
        addComponent(gpu1);
        
        HardwareComponent gpu2 = new HardwareComponent();
        gpu2.setName("AMD Radeon RX 7800 XT");
        gpu2.setBrand("AMD");
        gpu2.setType("gpu");
        gpu2.setCategory("RX 7000系");
        gpu2.setSpecifications("{\"series\":\"RX 7800 XT\",\"memory\":16,\"memoryType\":\"GDDR6\",\"powerConsumption\":263}");
        gpu2.setPrice(new BigDecimal("3999"));
        addComponent(gpu2);
    }
    
    private void initializeStorageData() {
        HardwareComponent storage1 = new HardwareComponent();
        storage1.setName("三星 980 PRO 1TB");
        storage1.setBrand("三星");
        storage1.setType("storage");
        storage1.setCategory("NVMe SSD");
        storage1.setSpecifications("{\"type\":\"NVMe SSD\",\"capacity\":1024,\"interface\":\"M.2\",\"readSpeed\":7000,\"writeSpeed\":5000}");
        storage1.setPrice(new BigDecimal("699"));
        addComponent(storage1);
        
        HardwareComponent storage2 = new HardwareComponent();
        storage2.setName("西数 WD Blue 2TB");
        storage2.setBrand("西数");
        storage2.setType("storage");
        storage2.setCategory("HDD");
        storage2.setSpecifications("{\"type\":\"HDD\",\"capacity\":2048,\"interface\":\"SATA\",\"rpm\":7200}");
        storage2.setPrice(new BigDecimal("399"));
        addComponent(storage2);
    }
    
    private void initializePSUData() {
        HardwareComponent psu1 = new HardwareComponent();
        psu1.setName("海盗船 RM850x");
        psu1.setBrand("海盗船");
        psu1.setType("psu");
        psu1.setCategory("全模组");
        psu1.setSpecifications("{\"wattage\":850,\"efficiency\":\"80+ Gold\",\"modular\":true,\"warranty\":10}");
        psu1.setPrice(new BigDecimal("899"));
        addComponent(psu1);
        
        HardwareComponent psu2 = new HardwareComponent();
        psu2.setName("安钛克 NE650G");
        psu2.setBrand("安钛克");
        psu2.setType("psu");
        psu2.setCategory("半模组");
        psu2.setSpecifications("{\"wattage\":650,\"efficiency\":\"80+ Gold\",\"modular\":false,\"warranty\":7}");
        psu2.setPrice(new BigDecimal("459"));
        addComponent(psu2);
    }
    
    private void initializeCaseData() {
        HardwareComponent case1 = new HardwareComponent();
        case1.setName("追风者 P400A");
        case1.setBrand("追风者");
        case1.setType("case");
        case1.setCategory("中塔");
        case1.setSpecifications("{\"formFactor\":\"Mid-Tower\",\"maxGPULength\":420,\"maxCPUCoolerHeight\":160,\"fans\":3}");
        case1.setPrice(new BigDecimal("599"));
        addComponent(case1);
        
        HardwareComponent case2 = new HardwareComponent();
        case2.setName("酷冷至尊 MasterBox TD500");
        case2.setBrand("酷冷至尊");
        case2.setType("case");
        case2.setCategory("中塔");
        case2.setSpecifications("{\"formFactor\":\"Mid-Tower\",\"maxGPULength\":410,\"maxCPUCoolerHeight\":165,\"fans\":3}");
        case2.setPrice(new BigDecimal("699"));
        addComponent(case2);
    }
    
    @Override
    public List<HardwareComponent> getAllComponentsForAdmin(int page, int size, String search, String type) {
        int offset = page * size;
        List<HardwareComponent> components;
        
        if (search != null && !search.trim().isEmpty()) {
            // 如果有搜索条件
            if (type != null && !type.trim().isEmpty()) {
                // 按类型和关键词搜索
                components = hardwareComponentMapper.searchByTypeAndKeyword(type.trim(), search.trim(), offset, size);
            } else {
                // 只按关键词搜索
                components = hardwareComponentMapper.searchByKeywordWithPagination(search.trim(), offset, size);
            }
        } else if (type != null && !type.trim().isEmpty()) {
            // 只按类型筛选
            components = hardwareComponentMapper.findByTypeWithPagination(type.trim(), offset, size);
        } else {
            // 获取所有组件
            components = hardwareComponentMapper.findAllWithPagination(offset, size);
        }
        
        return components;
    }
}