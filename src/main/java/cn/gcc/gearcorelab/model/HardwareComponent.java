package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class HardwareComponent {
    private Long id;
    private String name;           // 产品名称
    private String brand;          // 品牌
    private String type;           // 组件类型 (cpu, motherboard, ram, gpu, storage, psu, case)
    private String category;       // 细分类别 (如显卡的系列)
    private String specifications; // 规格描述 (JSON格式存储详细规格)
    private BigDecimal price;      // 价格
    private String imageUrl;       // 产品图片URL
    private Boolean isActive;      // 是否启用
    private Integer sortOrder;     // 排序顺序
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 构造函数
    public HardwareComponent() {
        this.isActive = true;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}