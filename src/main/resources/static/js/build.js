// 装机配置页面JavaScript

// 全局变量
let currentConfig = {
    id: null,
    title: '',
    components: {
        cpu: null,
        motherboard: null,
        ram: null,
        gpu: null,
        storage: [],
        psu: null,
        case: null
    }
};

// 最大配置单数量
const MAX_CONFIGS = 10;

// 自定义alert函数
function showCustomAlert(message, type = 'info', title = '') {
    return new Promise((resolve) => {
        // 创建遮罩层
        const overlay = document.createElement('div');
        overlay.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 10000;
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;
        `;
        
        // 创建弹窗
        const alert = document.createElement('div');
        alert.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%) scale(0.8);
            z-index: 10001;
            transition: transform 0.3s ease;
        `;
        
        const iconMap = {
            'info': '💡',
            'warning': '⚠️',
            'error': '❌',
            'success': '✅'
        };
        
        const colorMap = {
            'info': '#007bff',
            'warning': '#ffc107',
            'error': '#dc3545',
            'success': '#28a745'
        };
        
        alert.innerHTML = `
            <div style="
                background: white;
                border-radius: 12px;
                padding: 24px;
                max-width: 400px;
                width: 90%;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
                text-align: center;
            ">
                <div style="font-size: 48px; margin-bottom: 16px;">${iconMap[type] || iconMap.info}</div>
                ${title ? `<h3 style="margin: 0 0 12px 0; color: #333; font-size: 18px;">${title}</h3>` : ''}
                <p style="margin: 0 0 20px 0; color: #666; font-size: 16px; line-height: 1.5;">${message}</p>
                <button style="
                    padding: 10px 24px;
                    border: none;
                    background: ${colorMap[type] || colorMap.info};
                    color: white;
                    border-radius: 6px;
                    cursor: pointer;
                    font-size: 14px;
                    font-weight: 500;
                    transition: all 0.2s ease;
                " onmouseover="this.style.opacity='0.9'" onmouseout="this.style.opacity='1'" onclick="closeCustomAlert()">确定</button>
            </div>
        `;
        
        document.body.appendChild(overlay);
        document.body.appendChild(alert);
        
        // 显示动画
        setTimeout(() => {
            overlay.style.opacity = '1';
            alert.style.transform = 'translate(-50%, -50%) scale(1)';
        }, 10);
        
        // 关闭函数
        window.closeCustomAlert = () => {
            overlay.style.opacity = '0';
            alert.style.transform = 'translate(-50%, -50%) scale(0.8)';
            
            setTimeout(() => {
                document.body.removeChild(overlay);
                document.body.removeChild(alert);
                delete window.closeCustomAlert;
                resolve();
            }, 300);
        };
        
        // 点击遮罩层关闭
        overlay.addEventListener('click', () => window.closeCustomAlert());
    });
}

// 性能评分权重
const PERFORMANCE_WEIGHTS = {
    cpu: 0.3,
    gpu: 0.4,
    ram: 0.2,
    storage: 0.1
};

// 模拟硬件数据库
// 硬件数据缓存
let hardwareDatabase = {};

// 从后端API获取硬件数据
async function loadHardwareData() {
    try {
        const response = await fetch('/api/hardware/components');
        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                // 转换后端数据格式为前端期望的格式
                hardwareDatabase = transformBackendData(data.data);
                console.log('硬件数据加载成功');
            } else {
                console.error('获取硬件数据失败:', data.message);
                // 如果API失败，使用默认数据
                hardwareDatabase = getDefaultHardwareData();
            }
        } else {
            console.error('API请求失败:', response.status);
            // 如果API失败，使用默认数据
            hardwareDatabase = getDefaultHardwareData();
        }
    } catch (error) {
        console.error('加载硬件数据时出错:', error);
        // 如果出错，使用默认数据
        hardwareDatabase = getDefaultHardwareData();
    }
}

// 转换后端数据格式为前端期望的格式
function transformBackendData(backendData) {
    const transformedData = {};
    
    // 遍历后端返回的分组数据
    for (const [type, components] of Object.entries(backendData)) {
        transformedData[type] = components.map(component => {
            // 解析specifications JSON字符串
            let specs = {};
            try {
                specs = component.specifications ? JSON.parse(component.specifications) : {};
            } catch (e) {
                console.warn('解析规格数据失败:', component.specifications);
                specs = {};
            }
            
            return {
                id: component.id,
                name: component.name,
                brand: component.brand,
                price: component.price,
                specs: specs.description || component.specifications || '',
                // 根据不同类型添加特定字段
                ...getTypeSpecificFields(type, specs, component)
            };
        });
    }
    
    return transformedData;
}

// 根据组件类型获取特定字段
function getTypeSpecificFields(type, specs, component) {
    switch (type) {
        case 'cpu':
            return {
                generation: specs.generation || '',
                socket: specs.socket || '',
                cores: specs.cores || 0,
                threads: specs.threads || 0
            };
        case 'motherboard':
            return {
                chipset: specs.chipset || '',
                socket: specs.socket || '',
                memoryType: specs.memoryType || ''
            };
        case 'ram':
            return {
                type: specs.type || '',
                speed: specs.speed || 0,
                capacity: specs.capacity || 0
            };
        case 'gpu':
            return {
                series: specs.series || '',
                memory: specs.memory || 0,
                memoryType: specs.memoryType || ''
            };
        case 'storage':
            return {
                type: specs.type || '',
                capacity: specs.capacity || 0,
                interface: specs.interface || ''
            };
        case 'psu':
            return {
                wattage: specs.wattage || 0,
                efficiency: specs.efficiency || '',
                modular: specs.modular || false
            };
        case 'case':
            return {
                formFactor: specs.form_factor || specs.formFactor || ''
            };
        default:
            return {};
    }
}

// 默认硬件数据（作为备用）
function getDefaultHardwareData() {
    return {
    cpu: [
        // AMD 5代
        { id: 1, name: 'AMD Ryzen 5 5600X', brand: 'AMD', generation: '5代', socket: 'AM4', cores: 6, threads: 12, price: 1299, specs: '6核12线程 3.7GHz' },
        { id: 2, name: 'AMD Ryzen 7 5700X', brand: 'AMD', generation: '5代', socket: 'AM4', cores: 8, threads: 16, price: 1699, specs: '8核16线程 3.4GHz' },
        { id: 3, name: 'AMD Ryzen 7 5800X', brand: 'AMD', generation: '5代', socket: 'AM4', cores: 8, threads: 16, price: 2299, specs: '8核16线程 3.8GHz' },
        { id: 4, name: 'AMD Ryzen 9 5900X', brand: 'AMD', generation: '5代', socket: 'AM4', cores: 12, threads: 24, price: 3299, specs: '12核24线程 3.7GHz' },
        { id: 5, name: 'AMD Ryzen 9 5950X', brand: 'AMD', generation: '5代', socket: 'AM4', cores: 16, threads: 32, price: 4999, specs: '16核32线程 3.4GHz' },
        
        // AMD 7代
        { id: 6, name: 'AMD Ryzen 5 7600X', brand: 'AMD', generation: '7代', socket: 'AM5', cores: 6, threads: 12, price: 1899, specs: '6核12线程 4.7GHz' },
        { id: 7, name: 'AMD Ryzen 7 7700X', brand: 'AMD', generation: '7代', socket: 'AM5', cores: 8, threads: 16, price: 2699, specs: '8核16线程 4.5GHz' },
        { id: 8, name: 'AMD Ryzen 7 7800X3D', brand: 'AMD', generation: '7代', socket: 'AM5', cores: 8, threads: 16, price: 3299, specs: '8核16线程 4.2GHz 3D缓存' },
        { id: 9, name: 'AMD Ryzen 9 7900X', brand: 'AMD', generation: '7代', socket: 'AM5', cores: 12, threads: 24, price: 3999, specs: '12核24线程 4.7GHz' },
        { id: 10, name: 'AMD Ryzen 9 7950X', brand: 'AMD', generation: '7代', socket: 'AM5', cores: 16, threads: 32, price: 5499, specs: '16核32线程 4.5GHz' },
        
        // AMD 9代
        { id: 11, name: 'AMD Ryzen 5 9600X', brand: 'AMD', generation: '9代', socket: 'AM5', cores: 6, threads: 12, price: 2199, specs: '6核12线程 3.9GHz' },
        { id: 12, name: 'AMD Ryzen 7 9700X', brand: 'AMD', generation: '9代', socket: 'AM5', cores: 8, threads: 16, price: 2899, specs: '8核16线程 3.8GHz' },
        { id: 13, name: 'AMD Ryzen 9 9900X', brand: 'AMD', generation: '9代', socket: 'AM5', cores: 12, threads: 24, price: 4299, specs: '12核24线程 4.4GHz' },
        { id: 14, name: 'AMD Ryzen 9 9950X', brand: 'AMD', generation: '9代', socket: 'AM5', cores: 16, threads: 32, price: 5999, specs: '16核32线程 4.3GHz' },
        
        // Intel 12代
        { id: 15, name: 'Intel Core i5-12400F', brand: 'Intel', generation: '12代', socket: 'LGA1700', cores: 6, threads: 12, price: 1199, specs: '6核12线程 2.5GHz' },
        { id: 16, name: 'Intel Core i5-12600K', brand: 'Intel', generation: '12代', socket: 'LGA1700', cores: 10, threads: 16, price: 1899, specs: '10核16线程 3.7GHz' },
        { id: 17, name: 'Intel Core i7-12700K', brand: 'Intel', generation: '12代', socket: 'LGA1700', cores: 12, threads: 20, price: 2599, specs: '12核20线程 3.6GHz' },
        { id: 18, name: 'Intel Core i9-12900K', brand: 'Intel', generation: '12代', socket: 'LGA1700', cores: 16, threads: 24, price: 3999, specs: '16核24线程 3.2GHz' },
        
        // Intel 13代
        { id: 19, name: 'Intel Core i5-13400F', brand: 'Intel', generation: '13代', socket: 'LGA1700', cores: 10, threads: 16, price: 1399, specs: '10核16线程 2.5GHz' },
        { id: 20, name: 'Intel Core i5-13600K', brand: 'Intel', generation: '13代', socket: 'LGA1700', cores: 14, threads: 20, price: 2199, specs: '14核20线程 3.5GHz' },
        { id: 21, name: 'Intel Core i7-13700K', brand: 'Intel', generation: '13代', socket: 'LGA1700', cores: 16, threads: 24, price: 2999, specs: '16核24线程 3.4GHz' },
        { id: 22, name: 'Intel Core i9-13900K', brand: 'Intel', generation: '13代', socket: 'LGA1700', cores: 24, threads: 32, price: 4599, specs: '24核32线程 3.0GHz' },
        
        // Intel 14代
        { id: 23, name: 'Intel Core i5-14400F', brand: 'Intel', generation: '14代', socket: 'LGA1700', cores: 10, threads: 16, price: 1499, specs: '10核16线程 2.5GHz' },
        { id: 24, name: 'Intel Core i7-14700K', brand: 'Intel', generation: '14代', socket: 'LGA1700', cores: 20, threads: 28, price: 3199, specs: '20核28线程 3.4GHz' },
        { id: 25, name: 'Intel Core i9-14900K', brand: 'Intel', generation: '14代', socket: 'LGA1700', cores: 24, threads: 32, price: 4899, specs: '24核32线程 3.2GHz' },
        
        // Intel 15代
        { id: 26, name: 'Intel Core Ultra 5 155H', brand: 'Intel', generation: '15代', socket: 'LGA1700', cores: 12, threads: 16, price: 1799, specs: '12核16线程 3.8GHz' },
        { id: 27, name: 'Intel Core Ultra 7 165H', brand: 'Intel', generation: '15代', socket: 'LGA1700', cores: 16, threads: 22, price: 2799, specs: '16核22线程 3.8GHz' },
        { id: 28, name: 'Intel Core Ultra 9 185H', brand: 'Intel', generation: '15代', socket: 'LGA1700', cores: 16, threads: 22, price: 3599, specs: '16核22线程 3.8GHz' }
    ],
    
    motherboard: [
        // AMD主板
        { id: 1, name: 'MSI B450M PRO-VDH MAX', brand: 'MSI', chipset: 'B450', socket: 'AM4', memoryType: 'DDR4', price: 399, specs: 'Micro-ATX, DDR4-3200' },
        { id: 2, name: 'ASUS TUF GAMING B550M-PLUS', brand: 'ASUS', chipset: 'B550', socket: 'AM4', memoryType: 'DDR4', price: 699, specs: 'Micro-ATX, DDR4-4400' },
        { id: 3, name: 'MSI MAG B550 TOMAHAWK', brand: 'MSI', chipset: 'B550', socket: 'AM4', memoryType: 'DDR4', price: 899, specs: 'ATX, DDR4-4400' },
        { id: 4, name: 'ASUS ROG STRIX X570-E GAMING', brand: 'ASUS', chipset: 'X570', socket: 'AM4', memoryType: 'DDR4', price: 1599, specs: 'ATX, DDR4-4400' },
        { id: 5, name: 'MSI PRO B650M-A WIFI', brand: 'MSI', chipset: 'B650', socket: 'AM5', memoryType: 'DDR5', price: 799, specs: 'Micro-ATX, DDR5-5200' },
        { id: 6, name: 'ASUS TUF GAMING B650-PLUS WIFI', brand: 'ASUS', chipset: 'B650', socket: 'AM5', memoryType: 'DDR5', price: 999, specs: 'ATX, DDR5-5200' },
        { id: 7, name: 'MSI MAG X670E TOMAHAWK WIFI', brand: 'MSI', chipset: 'X670E', socket: 'AM5', memoryType: 'DDR5', price: 1799, specs: 'ATX, DDR5-5600' },
        
        // Intel主板
        { id: 8, name: 'MSI PRO B660M-A WIFI DDR4', brand: 'MSI', chipset: 'B660', socket: 'LGA1700', memoryType: 'DDR4', price: 599, specs: 'Micro-ATX, DDR4-4800' },
        { id: 9, name: 'ASUS PRIME B660-PLUS D4', brand: 'ASUS', chipset: 'B660', socket: 'LGA1700', memoryType: 'DDR4', price: 699, specs: 'ATX, DDR4-4800' },
        { id: 10, name: 'MSI MAG Z690 TOMAHAWK WIFI DDR4', brand: 'MSI', chipset: 'Z690', socket: 'LGA1700', memoryType: 'DDR4', price: 1299, specs: 'ATX, DDR4-5200' },
        { id: 11, name: 'ASUS ROG STRIX Z690-E GAMING WIFI', brand: 'ASUS', chipset: 'Z690', socket: 'LGA1700', memoryType: 'DDR5', price: 1999, specs: 'ATX, DDR5-6000' },
        { id: 12, name: 'MSI PRO B760M-A WIFI DDR4', brand: 'MSI', chipset: 'B760', socket: 'LGA1700', memoryType: 'DDR4', price: 649, specs: 'Micro-ATX, DDR4-5000' },
        { id: 13, name: 'ASUS PRIME Z790-P WIFI', brand: 'ASUS', chipset: 'Z790', socket: 'LGA1700', memoryType: 'DDR5', price: 1599, specs: 'ATX, DDR5-5600' }
    ],
    
    ram: [
        // DDR4内存
        { id: 1, name: '金士顿 FURY Beast DDR4 3200 16GB', brand: '金士顿', type: 'DDR4', speed: 3200, capacity: 16, price: 299, specs: '16GB DDR4-3200 CL16' },
        { id: 2, name: '海盗船 Vengeance LPX DDR4 3200 32GB', brand: '海盗船', type: 'DDR4', speed: 3200, capacity: 32, price: 599, specs: '32GB DDR4-3200 CL16' },
        { id: 3, name: 'G.SKILL Ripjaws V DDR4 3600 16GB', brand: 'G.SKILL', type: 'DDR4', speed: 3600, capacity: 16, price: 399, specs: '16GB DDR4-3600 CL16' },
        { id: 4, name: '金士顿 FURY Beast DDR4 3600 32GB', brand: '金士顿', type: 'DDR4', speed: 3600, capacity: 32, price: 699, specs: '32GB DDR4-3600 CL17' },
        
        // DDR5内存
        { id: 5, name: '金士顿 FURY Beast DDR5 5200 16GB', brand: '金士顿', type: 'DDR5', speed: 5200, capacity: 16, price: 499, specs: '16GB DDR5-5200 CL40' },
        { id: 6, name: '海盗船 Dominator Platinum DDR5 5600 32GB', brand: '海盗船', type: 'DDR5', speed: 5600, capacity: 32, price: 1299, specs: '32GB DDR5-5600 CL36' },
        { id: 7, name: 'G.SKILL Trident Z5 DDR5 6000 16GB', brand: 'G.SKILL', type: 'DDR5', speed: 6000, capacity: 16, price: 799, specs: '16GB DDR5-6000 CL30' },
        { id: 8, name: '金士顿 FURY Beast DDR5 5600 32GB', brand: '金士顿', type: 'DDR5', speed: 5600, capacity: 32, price: 999, specs: '32GB DDR5-5600 CL40' }
    ],
    
    gpu: [
        { id: 1, name: 'NVIDIA GeForce RTX 4060', brand: 'NVIDIA', series: 'RTX 40', memory: 8, powerConsumption: 115, price: 2299, specs: '8GB GDDR6, 115W' },
        { id: 2, name: 'NVIDIA GeForce RTX 4060 Ti', brand: 'NVIDIA', series: 'RTX 40', memory: 16, powerConsumption: 165, price: 3199, specs: '16GB GDDR6, 165W' },
        { id: 3, name: 'NVIDIA GeForce RTX 4070', brand: 'NVIDIA', series: 'RTX 40', memory: 12, powerConsumption: 200, price: 4599, specs: '12GB GDDR6X, 200W' },
        { id: 4, name: 'NVIDIA GeForce RTX 4070 Ti', brand: 'NVIDIA', series: 'RTX 40', memory: 12, powerConsumption: 285, price: 6299, specs: '12GB GDDR6X, 285W' },
        { id: 5, name: 'NVIDIA GeForce RTX 4080', brand: 'NVIDIA', series: 'RTX 40', memory: 16, powerConsumption: 320, price: 8999, specs: '16GB GDDR6X, 320W' },
        { id: 6, name: 'AMD Radeon RX 7600', brand: 'AMD', series: 'RX 7000', memory: 8, powerConsumption: 165, price: 1999, specs: '8GB GDDR6, 165W' },
        { id: 7, name: 'AMD Radeon RX 7700 XT', brand: 'AMD', series: 'RX 7000', memory: 12, powerConsumption: 245, price: 3499, specs: '12GB GDDR6, 245W' },
        { id: 8, name: 'AMD Radeon RX 7800 XT', brand: 'AMD', series: 'RX 7000', memory: 16, powerConsumption: 263, price: 4299, specs: '16GB GDDR6, 263W' }
    ],
    
    storage: [
        { id: 1, name: '三星 980 NVMe SSD 500GB', brand: '三星', type: 'NVMe SSD', capacity: 500, interface: 'M.2', price: 399, specs: '500GB NVMe PCIe 3.0' },
        { id: 2, name: '西数 SN770 NVMe SSD 1TB', brand: '西数', type: 'NVMe SSD', capacity: 1000, interface: 'M.2', price: 599, specs: '1TB NVMe PCIe 4.0' },
        { id: 3, name: '三星 980 PRO NVMe SSD 2TB', brand: '三星', type: 'NVMe SSD', capacity: 2000, interface: 'M.2', price: 1299, specs: '2TB NVMe PCIe 4.0' },
        { id: 4, name: '希捷 酷鱼 HDD 2TB', brand: '希捷', type: 'HDD', capacity: 2000, interface: 'SATA', price: 399, specs: '2TB 7200RPM SATA' },
        { id: 5, name: '西数 蓝盘 HDD 4TB', brand: '西数', type: 'HDD', capacity: 4000, interface: 'SATA', price: 699, specs: '4TB 5400RPM SATA' }
    ],
    
    psu: [
        { id: 1, name: '海盗船 CV550 550W', brand: '海盗船', wattage: 550, efficiency: '80+ Bronze', modular: false, price: 399, specs: '550W 80+ Bronze 非模组' },
        { id: 2, name: '海韵 FOCUS GX-650 650W', brand: '海韵', wattage: 650, efficiency: '80+ Gold', modular: true, price: 699, specs: '650W 80+ Gold 全模组' },
        { id: 3, name: '安钛克 HCG750 750W', brand: '安钛克', wattage: 750, efficiency: '80+ Gold', modular: true, price: 899, specs: '750W 80+ Gold 全模组' },
        { id: 4, name: '海盗船 RM850x 850W', brand: '海盗船', wattage: 850, efficiency: '80+ Gold', modular: true, price: 1199, specs: '850W 80+ Gold 全模组' },
        { id: 5, name: '海韵 PRIME TX-1000 1000W', brand: '海韵', wattage: 1000, efficiency: '80+ Titanium', modular: true, price: 1899, specs: '1000W 80+ Titanium 全模组' }
    ],
    
    case: [
        { id: 1, name: '酷冷至尊 MasterBox Q300L', brand: '酷冷至尊', formFactor: 'Mini-ITX', price: 299, specs: 'Mini-ITX 紧凑型机箱' },
        { id: 2, name: '追风者 P300A', brand: '追风者', formFactor: 'Mid-Tower', price: 399, specs: 'Mid-Tower ATX 机箱' },
        { id: 3, name: '海盗船 4000D', brand: '海盗船', formFactor: 'Mid-Tower', price: 699, specs: 'Mid-Tower ATX 钢化玻璃' },
        { id: 4, name: 'NZXT H510', brand: 'NZXT', formFactor: 'Mid-Tower', price: 599, specs: 'Mid-Tower ATX 简约设计' },
        { id: 5, name: '联力 O11 Dynamic', brand: '联力', formFactor: 'Full-Tower', price: 1299, specs: 'Full-Tower ATX 双面玻璃' }
    ]
    };
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', async function() {
    // 首先加载硬件数据
    await loadHardwareData();
    loadConfigList();
    hideAllSearchResults();
    
    // 修改保存按钮的点击事件
    const saveBtn = document.getElementById('saveBtn');
    const fixedSaveBtn = document.getElementById('fixedSaveBtn');
    if (saveBtn) {
        saveBtn.onclick = showSaveModal;
    }
    if (fixedSaveBtn) {
        fixedSaveBtn.onclick = showSaveModal;
    }
    
    // 为所有搜索输入框添加事件监听
    document.querySelectorAll('.search-input').forEach(input => {
        input.addEventListener('blur', function(e) {
            // 延迟隐藏，以便点击搜索结果
            setTimeout(() => {
                if (!e.target.closest('.component-search').querySelector('.search-results:hover')) {
                    hideAllSearchResults();
                }
            }, 200);
        });
    });
    
    // 点击模态框背景关闭
    const modal = document.getElementById('saveModal');
    if (modal) {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                hideSaveModal();
            }
        });
    }
    
    // ESC键关闭模态框
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            hideSaveModal();
        }
    });
});

// 检查硬件数据是否已加载
function isHardwareDataLoaded() {
    return hardwareDatabase && Object.keys(hardwareDatabase).length > 0;
}

// 搜索组件
function searchComponent(type, query) {
    const resultsContainer = document.getElementById(type + '-results');
    
    // 如果硬件数据还未加载，显示加载提示
    if (!isHardwareDataLoaded()) {
        resultsContainer.innerHTML = '<div class="search-result-item">正在加载硬件数据...</div>';
        resultsContainer.style.display = 'block';
        return;
    }
    
    const components = hardwareDatabase[type] || [];
    
    let filteredComponents;
    if (!query || query.length === 0) {
        // 如果没有查询内容，显示所有组件
        filteredComponents = components;
    } else {
        // 过滤组件
        filteredComponents = components.filter(component => 
            component.name.toLowerCase().includes(query.toLowerCase()) ||
            component.brand.toLowerCase().includes(query.toLowerCase())
        );
    }
    
    displaySearchResults(type, filteredComponents);
}

// 显示搜索结果
function displaySearchResults(type, components) {
    const resultsContainer = document.getElementById(type + '-results');
    
    if (components.length === 0) {
        resultsContainer.innerHTML = '<div class="search-result-item">未找到相关配件</div>';
        resultsContainer.style.display = 'block';
        return;
    }
    
    resultsContainer.innerHTML = components.map(component => {
        const translatedSpecs = translateSpecs(component.specs, type);
        return `<div class="search-result-item" onclick="selectComponent('${type}', ${component.id})">
            <div class="search-item-header">
                <strong>${component.name}</strong>
                <span class="search-item-price">¥${component.price}</span>
            </div>
            <div class="search-item-specs">${translatedSpecs}</div>
        </div>`;
    }).join('');
    
    resultsContainer.style.display = 'block';
}

// 显示搜索结果框
function showSearchResults(type) {
    hideAllSearchResults();
    const input = event.target;
    // 无论输入框是否有内容，都显示搜索结果
    searchComponent(type, input.value);
}

// 隐藏所有搜索结果
function hideAllSearchResults() {
    const allResults = document.querySelectorAll('.search-results');
    allResults.forEach(result => {
        result.style.display = 'none';
    });
}

// 选择组件
function selectComponent(type, componentId, slotIndex = 0) {
    // 检查硬件数据是否已加载
    if (!isHardwareDataLoaded()) {
        console.error('硬件数据尚未加载完成');
        return;
    }
    
    const component = hardwareDatabase[type].find(c => c.id === componentId);
    if (!component) return;
    
    if (type === 'storage') {
        // 存储设备支持多槽位
        if (!currentConfig.components.storage) {
            currentConfig.components.storage = [];
        }
        currentConfig.components.storage[slotIndex] = component;
        
        // 更新对应槽位的UI
        const slotId = slotIndex === 0 ? 'storage' : `storage-${slotIndex}`;
        document.getElementById(slotId + '-name').textContent = component.name;
        document.getElementById(slotId + '-specs').textContent = translateSpecs(component.specs, type);
        document.getElementById(slotId + '-price').textContent = '¥' + component.price;
        document.getElementById(slotId + '-selected').classList.add('active');
        
        // 更新搜索框
        const searchInput = document.querySelector(`#${slotId}-results`).previousElementSibling;
        searchInput.value = component.name;
        
        // 隐藏搜索结果
        document.getElementById(slotId + '-results').style.display = 'none';
    } else {
        // 其他组件单槽位
        currentConfig.components[type] = component;
        
        // 更新UI显示
        document.getElementById(type + '-name').textContent = component.name;
        document.getElementById(type + '-specs').textContent = translateSpecs(component.specs, type);
        document.getElementById(type + '-price').textContent = '¥' + component.price;
        document.getElementById(type + '-selected').classList.add('active');
        
        // 隐藏搜索结果
        document.getElementById(type + '-results').style.display = 'none';
        
        // 清空搜索框
        const searchInput = document.querySelector(`#${type}-results`).previousElementSibling;
        searchInput.value = component.name;
    }
    
    // 显示保存按钮
    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) {
        saveBtn.style.display = 'block';
    }
    const fixedSaveBtn = document.getElementById('fixedSaveBtn');
    if (fixedSaveBtn) {
        fixedSaveBtn.style.display = 'flex';
    }
    
    // 启用滚动（有配置时）
    const mainContentBody = document.querySelector('.main-content-body');
    if (mainContentBody) {
        mainContentBody.classList.remove('no-scroll');
    }
    
    // 检查兼容性和更新性能评分
    checkCompatibility();
    updatePerformanceScore();
}

// 移除组件
function removeComponent(type, slotIndex = 0) {
    if (type === 'storage') {
        // 移除存储设备
        if (currentConfig.components.storage && currentConfig.components.storage[slotIndex]) {
            currentConfig.components.storage[slotIndex] = null;
            
            // 清理数组中的空值
            currentConfig.components.storage = currentConfig.components.storage.filter(item => item !== null);
            
            const slotId = slotIndex === 0 ? 'storage' : `storage-${slotIndex}`;
            document.getElementById(slotId + '-selected').classList.remove('active');
            
            // 清空搜索框
            const searchInput = document.querySelector(`#${slotId}-results`).previousElementSibling;
            searchInput.value = '';
            
            // 如果是额外槽位，移除整个槽位
            if (slotIndex > 0) {
                const slotElement = document.getElementById(`storage-slot-${slotIndex}`);
                if (slotElement) {
                    slotElement.remove();
                }
            }
        }
    } else {
        currentConfig.components[type] = null;
        document.getElementById(type + '-selected').classList.remove('active');
        
        // 清空搜索框
        const searchInput = document.querySelector(`#${type}-results`).previousElementSibling;
        searchInput.value = '';
    }
    
    // 检查兼容性和更新性能评分
    checkCompatibility();
    updatePerformanceScore();
    
    // 检查是否还有组件，决定是否显示保存按钮
    const hasComponents = hasAnyComponents();
    if (!hasComponents) {
        const saveBtn = document.getElementById('saveBtn');
        if (saveBtn) {
            saveBtn.style.display = 'none';
        }
        const fixedSaveBtn = document.getElementById('fixedSaveBtn');
        if (fixedSaveBtn) {
            fixedSaveBtn.style.display = 'none';
        }
    }
}

// 检查兼容性
function checkCompatibility() {
    const { cpu, motherboard, ram, gpu, psu, storage } = currentConfig.components;
    const statusDiv = document.getElementById('compatibilityStatus');
    const messageSpan = document.getElementById('compatibilityMessage');
    
    let issues = [];
    
    // CPU和主板兼容性检查
    if (cpu && motherboard) {
        if (cpu.socket !== motherboard.socket) {
            issues.push(`CPU接口(${cpu.socket})与主板接口(${motherboard.socket})不兼容`);
        }
    }
    
    // 内存和主板兼容性检查
    if (ram && motherboard) {
        if (ram.type !== motherboard.memoryType) {
            issues.push(`内存类型(${ram.type})与主板内存类型(${motherboard.memoryType})不兼容`);
        }
    }
    
    // 电源功率检查
    if (psu && (cpu || gpu)) {
        let totalPower = 100; // 基础功耗
        if (cpu) {
            totalPower += cpu.cores * 15; // 估算CPU功耗
        }
        if (gpu) {
            totalPower += gpu.powerConsumption;
        }
        
        // 获取电源功率，优先从wattage字段，然后从specs中解析
        let psuWattage = psu.wattage;
        if (!psuWattage || psuWattage === 0) {
            if (psu.specs) {
                try {
                    const specsObj = typeof psu.specs === 'string' ? JSON.parse(psu.specs) : psu.specs;
                    if (specsObj.wattage) {
                        const wattageStr = specsObj.wattage.toString();
                        const wattageMatch = wattageStr.match(/\d+/);
                        if (wattageMatch) {
                            psuWattage = parseInt(wattageMatch[0]);
                        }
                    }
                } catch (e) {
                    console.warn('Failed to parse PSU specs:', e);
                }
            }
        }
        
        if (psuWattage && psuWattage < totalPower * 1.2) { // 建议电源功率为总功耗的1.2倍
            issues.push(`电源功率(${psuWattage}W)可能不足，建议至少${Math.ceil(totalPower * 1.2)}W`);
        }
    }
    
    // 显示兼容性结果
    if (issues.length > 0) {
        statusDiv.className = 'compatibility-status incompatible';
        messageSpan.innerHTML = '<i class="fas fa-exclamation-triangle"></i> ' + issues.join('<br>');
        statusDiv.style.display = 'block';
    } else if (hasAnyComponents()) {
        statusDiv.className = 'compatibility-status compatible';
        messageSpan.innerHTML = '<i class="fas fa-check-circle"></i> 所有配件兼容性良好';
        statusDiv.style.display = 'block';
    } else {
        statusDiv.style.display = 'none';
    }
}

// 检查是否有任何组件
function hasAnyComponents() {
    const { cpu, motherboard, ram, gpu, storage, psu, case: caseComponent } = currentConfig.components;
    return cpu || motherboard || ram || gpu || (storage && storage.length > 0) || psu || caseComponent;
}

// 计算性能评分
function calculatePerformanceScore() {
    const { cpu, gpu, ram, storage } = currentConfig.components;
    let score = 0;
    
    // CPU评分 (基于核心数、频率和架构)
    if (cpu) {
        let cpuScore = 0;
        const cores = parseInt(cpu.cores) || 4;
        let frequency = 3.0; // 默认频率
        
        // 尝试从specs中解析频率
        if (cpu.specs && typeof cpu.specs === 'string') {
            const freqMatch = cpu.specs.match(/([0-9.]+)GHz/);
            if (freqMatch && freqMatch[1]) {
                const parsedFreq = parseFloat(freqMatch[1]);
                if (!isNaN(parsedFreq) && parsedFreq > 0) {
                    frequency = parsedFreq;
                }
            }
        }
        
        // 基础评分：核心数 * 频率的组合 (降低系数)
        cpuScore = (cores * 4) + (frequency * 6);
        
        // 品牌和代数加成
        if (cpu.brand === 'AMD') {
            if (cpu.generation === '7代') cpuScore *= 1.15;
            else if (cpu.generation === '5代') cpuScore *= 1.05;
        } else if (cpu.brand === 'Intel') {
            if (cpu.generation === '13代') cpuScore *= 1.1;
            else if (cpu.generation === '12代') cpuScore *= 1.05;
        }
        
        score += cpuScore * PERFORMANCE_WEIGHTS.cpu;
    }
    
    // GPU评分 (基于显存、架构和型号)
    if (gpu) {
        let gpuScore = 0;
        let memory = 4; // 默认显存
        
        // 安全解析显存容量
        if (gpu.memory) {
            const parsedMemory = parseInt(gpu.memory);
            if (!isNaN(parsedMemory) && parsedMemory > 0) {
                memory = parsedMemory;
            }
        }
        
        // 基础评分：显存容量 (降低系数)
        gpuScore = memory * 6;
        
        // 架构加成
        if (gpu.series === 'RTX 40') gpuScore *= 1.3;
        else if (gpu.series === 'RTX 30') gpuScore *= 1.1;
        else if (gpu.series === 'RX 7000') gpuScore *= 1.2;
        else if (gpu.series === 'RX 6000') gpuScore *= 1.05;
        
        // 高端型号额外加成
        if (gpu.name && (gpu.name.includes('4090') || gpu.name.includes('4080'))) {
            gpuScore *= 1.2;
        } else if (gpu.name && (gpu.name.includes('7900') || gpu.name.includes('7800'))) {
            gpuScore *= 1.15;
        }
        
        score += gpuScore * PERFORMANCE_WEIGHTS.gpu;
    }
    
    // 内存评分 (基于容量、频率和类型)
    if (ram) {
        let ramScore = 0;
        let capacity = 8; // 默认容量
        let speed = 3200; // 默认频率
        
        // 安全解析内存容量
        if (ram.capacity) {
            const parsedCapacity = parseInt(ram.capacity);
            if (!isNaN(parsedCapacity) && parsedCapacity > 0) {
                capacity = parsedCapacity;
            }
        }
        
        // 安全解析内存频率
        if (ram.speed) {
            const parsedSpeed = parseInt(ram.speed);
            if (!isNaN(parsedSpeed) && parsedSpeed > 0) {
                speed = parsedSpeed;
            }
        }
        
        // 基础评分：容量和频率 (降低系数)
        ramScore = (capacity * 1.5) + (speed / 400);
        
        // DDR类型加成
        if (ram.type === 'DDR5') ramScore *= 1.4;
        else if (ram.type === 'DDR4') ramScore *= 1.0;
        
        score += ramScore * PERFORMANCE_WEIGHTS.ram;
    }
    
    // 存储评分 (基于类型、容量和接口)
    if (storage && storage.length > 0) {
        let storageScore = 0;
        storage.forEach(drive => {
            if (drive) {
                let capacity = 500; // 默认容量
                
                // 安全解析存储容量
                if (drive.capacity) {
                    const parsedCapacity = parseInt(drive.capacity);
                    if (!isNaN(parsedCapacity) && parsedCapacity > 0) {
                        capacity = parsedCapacity;
                    }
                }
                
                let driveScore = capacity / 160;
                
                // 存储类型加成
                if (drive.type === 'NVMe SSD') driveScore *= 2.5;
                else if (drive.type === 'SATA SSD') driveScore *= 1.8;
                else if (drive.type === 'HDD') driveScore *= 0.8;
                
                storageScore += driveScore;
            }
        });
        score += storageScore * PERFORMANCE_WEIGHTS.storage;
    }
    
    // 确保评分在合理范围内，并处理NaN情况
    if (isNaN(score) || !isFinite(score)) {
        score = 0;
    }
    return Math.min(100, Math.max(0, Math.round(score * 10) / 10));
}

// 翻译硬件规格信息为中文
function translateSpecs(specs, type) {
    if (!specs) return '暂无规格信息';
    
    // 如果specs是JSON字符串，先解析
    let specsObj;
    if (typeof specs === 'string') {
        try {
            specsObj = JSON.parse(specs);
        } catch (e) {
            // 如果不是JSON，直接使用字符串
            specsObj = null;
        }
    } else if (typeof specs === 'object') {
        specsObj = specs;
    }
    
    // 英文到中文的映射
    const translations = {
        // 通用翻译
        'cores': '核心',
        'threads': '线程',
        'base_clock': '基础频率',
        'boost_clock': '加速频率',
        'game_clock': '游戏频率',
        'socket': '接口',
        'memory': '显存',
        'capacity': '容量',
        'speed': '频率',
        'interface': '接口',
        'form_factor': '规格',
        'wattage': '功率',
        'efficiency': '转换效率',
        'modular': '模组化',
        'warranty': '保修',
        'chipset': '芯片组',
        'max_memory': '最大内存',
        'memory_type': '内存类型',
        'memory_slots': '内存插槽',
        
        // CPU相关
        'GHz': 'GHz',
        'LGA1700': 'LGA1700',
        'AM5': 'AM5',
        'AM4': 'AM4',
        'tdp': 'TDP',
        
        // GPU相关
        'GDDR6X': 'GDDR6X',
        'GDDR6': 'GDDR6',
        'cuda_cores': 'CUDA核心',
        'stream_processors': '流处理器',
        'memory_bandwidth': '显存带宽',
        'MHz': 'MHz',
        
        // 主板相关
        'pcie_slots': 'PCIe插槽',
        'wifi': 'WiFi',
        'ethernet': '网卡',
        
        // 内存相关
        'DDR5': 'DDR5',
        'DDR4': 'DDR4',
        'timings': '时序',
        'voltage': '电压',
        'kit': '套装',
        'rgb': 'RGB灯效',
        
        // 存储相关
        'read_speed': '读取速度',
        'write_speed': '写入速度',
        'rpm': '转速',
        'cache': '缓存',
        'MB/s': 'MB/s',
        'GB/s': 'GB/s',
        'PCIe': 'PCIe',
        'SATA': 'SATA',
        'NVMe': 'NVMe',
        'M.2': 'M.2',
        
        // 电源相关
        'Gold': '金牌',
        'Platinum': '白金',
        'Titanium': '钛金',
        'Bronze': '铜牌',
        'pfc': 'PFC',
        'fan_size': '风扇尺寸',
        'cables': '线材',
        
        // 机箱相关
        'max_gpu_length': '最大显卡长度',
        'max_cpu_cooler': '最大散热器高度',
        'drive_bays': '硬盘位',
        'fans_included': '预装风扇',
        'side_panel': '侧板材质'
    };
    
    // 如果是对象，格式化为中文显示
    if (specsObj && typeof specsObj === 'object') {
        const translatedParts = [];
        
        // 根据硬件类型显示不同的关键信息
        if (type === 'cpu') {
            if (specsObj.cores) translatedParts.push(`${specsObj.cores}核心`);
            if (specsObj.threads) translatedParts.push(`${specsObj.threads}线程`);
            if (specsObj.base_clock) translatedParts.push(`基频${specsObj.base_clock}`);
            if (specsObj.boost_clock) translatedParts.push(`睿频${specsObj.boost_clock}`);
            if (specsObj.socket) translatedParts.push(`接口${specsObj.socket}`);
        } else if (type === 'gpu') {
            if (specsObj.memory) translatedParts.push(`显存${specsObj.memory}`);
            if (specsObj.cuda_cores) translatedParts.push(`${specsObj.cuda_cores}个CUDA核心`);
            if (specsObj.stream_processors) translatedParts.push(`${specsObj.stream_processors}个流处理器`);
            if (specsObj.boost_clock) translatedParts.push(`加速频率${specsObj.boost_clock}`);
        } else if (type === 'motherboard') {
            if (specsObj.chipset) translatedParts.push(`芯片组${specsObj.chipset}`);
            if (specsObj.socket) translatedParts.push(`接口${specsObj.socket}`);
            if (specsObj.memory_type) translatedParts.push(`支持${specsObj.memory_type}`);
            if (specsObj.max_memory) translatedParts.push(`最大${specsObj.max_memory}`);
        } else if (type === 'ram') {
            if (specsObj.capacity) translatedParts.push(`容量${specsObj.capacity}`);
            if (specsObj.speed) translatedParts.push(`频率${specsObj.speed}`);
            if (specsObj.timings) translatedParts.push(`时序${specsObj.timings}`);
            if (specsObj.kit) translatedParts.push(`套装${specsObj.kit}`);
        } else if (type === 'storage') {
            if (specsObj.capacity) translatedParts.push(`容量${specsObj.capacity}`);
            if (specsObj.interface) translatedParts.push(`接口${specsObj.interface}`);
            if (specsObj.read_speed) translatedParts.push(`读取${specsObj.read_speed}`);
            if (specsObj.form_factor) translatedParts.push(`规格${specsObj.form_factor}`);
        } else if (type === 'psu') {
            if (specsObj.wattage) translatedParts.push(`功率${specsObj.wattage}`);
            if (specsObj.efficiency) translatedParts.push(`效率${specsObj.efficiency}`);
            if (specsObj.modular) translatedParts.push(`${specsObj.modular}`);
        } else if (type === 'case') {
            if (specsObj.form_factor) translatedParts.push(`规格${specsObj.form_factor}`);
            if (specsObj.max_gpu_length) translatedParts.push(`最大显卡${specsObj.max_gpu_length}`);
            if (specsObj.side_panel) translatedParts.push(`侧板${specsObj.side_panel}`);
        }
        
        return translatedParts.join(' | ') || '暂无规格信息';
    }
    
    // 如果是字符串，进行翻译处理
    let translatedSpecs = specs.toString();
    
    // 替换英文关键词为中文
    Object.keys(translations).forEach(en => {
        const cn = translations[en];
        const regex = new RegExp(`\\b${en}\\b`, 'gi');
        translatedSpecs = translatedSpecs.replace(regex, cn);
    });
    
    // 特殊处理一些常见格式
    translatedSpecs = translatedSpecs
        .replace(/([0-9]+)x ([0-9.]+)英寸/g, '$1个 $2英寸')
        .replace(/([0-9]+)x PCIe/g, '$1个 PCIe')
        .replace(/([0-9]+)x ([0-9.]+)GB/g, '$1个 $2GB')
        .replace(/([0-9]+)x ([0-9.]+)TB/g, '$1个 $2TB')
        .replace(/CL([0-9]+)/g, 'CL$1')
        .replace(/([0-9.]+)V/g, '$1V')
        .replace(/([0-9]+)W/g, '$1W')
        .replace(/([0-9]+)年/g, '$1年')
        .replace(/80\+ /g, '80Plus ')
        .replace(/WiFi ([0-9]+[A-Z]*)/g, 'WiFi $1')
        .replace(/([0-9.]+)Gb/g, '$1Gb')
        .replace(/([0-9]+)RPM/g, '$1转/分')
        .replace(/([0-9]+)MB/g, '$1MB')
        .replace(/([0-9]+)GB/g, '$1GB')
        .replace(/([0-9]+)TB/g, '$1TB')
        .replace(/([0-9.]+)mm/g, '$1mm')
        .replace(/全模组/g, '全模组')
        .replace(/非模组/g, '非模组')
        .replace(/钢化玻璃/g, '钢化玻璃')
        .replace(/是/g, '支持')
        .replace(/否/g, '不支持')
        .replace(/无/g, '无');
    
    return translatedSpecs;
}

// 更新性能评分显示
function updatePerformanceScore() {
    const scoreElement = document.getElementById('performanceScore');
    const scoreNumber = document.getElementById('scoreNumber');
    const progressFill = document.getElementById('progressFill');
    
    if (hasAnyComponents()) {
        const newScore = calculatePerformanceScore();
        
        // 获取当前分数进行数字动画
        const currentScore = parseFloat(scoreNumber.textContent) || 0;
        
        // 数字动画
        const duration = 1000; // 动画持续时间
        const startTime = performance.now();
        
        function animateScore(timestamp) {
            const elapsed = timestamp - startTime;
            const progress = Math.min(elapsed / duration, 1);
            
            // 使用缓动函数
            const easeOutCubic = 1 - Math.pow(1 - progress, 3);
            const currentValue = currentScore + (newScore - currentScore) * easeOutCubic;
            
            scoreNumber.textContent = currentValue.toFixed(1);
            
            if (progress < 1) {
                requestAnimationFrame(animateScore);
            } else {
                scoreNumber.textContent = newScore.toFixed(1);
            }
        }
        
        requestAnimationFrame(animateScore);
        
        // 进度条动画（更平滑的过渡）
        progressFill.style.transition = 'width 1s cubic-bezier(0.25, 0.46, 0.45, 0.94), background 0.6s ease, box-shadow 0.6s ease';
        progressFill.style.width = newScore + '%';
        
        // 根据分数调整进度条颜色和发光效果
        let gradient, boxShadow;
        if (newScore < 20) {
            gradient = 'linear-gradient(90deg, #dc3545, #e74c3c)';
            boxShadow = '0 0 8px rgba(220, 53, 69, 0.4)';
        } else if (newScore < 40) {
            gradient = 'linear-gradient(90deg, #fd7e14, #ff6b35)';
            boxShadow = '0 0 8px rgba(253, 126, 20, 0.4)';
        } else if (newScore < 60) {
            gradient = 'linear-gradient(90deg, #ffc107, #ffb700)';
            boxShadow = '0 0 8px rgba(255, 193, 7, 0.4)';
        } else if (newScore < 80) {
            gradient = 'linear-gradient(90deg, #20c997, #17a2b8)';
            boxShadow = '0 0 10px rgba(32, 201, 151, 0.5)';
        } else {
            gradient = 'linear-gradient(90deg, #28a745, #20c997)';
            boxShadow = '0 0 12px rgba(40, 167, 69, 0.6)';
        }
        
        progressFill.style.background = gradient;
        progressFill.style.boxShadow = boxShadow;
        
        // 高分时添加脉冲效果
        if (newScore >= 85) {
            progressFill.style.animation = 'pulse 2s ease-in-out infinite';
        } else {
            progressFill.style.animation = 'none';
        }
        
        // 更新瓦数和价格
        updateConfigStats();
        
        scoreElement.style.display = 'block';
    } else {
        // 没有组件时显示0分
        if (scoreNumber) {
            scoreNumber.textContent = '0.0';
        }
        if (progressFill) {
            progressFill.style.width = '0%';
            progressFill.style.background = 'linear-gradient(90deg, #dc3545, #e74c3c)';
            progressFill.style.boxShadow = 'none';
            progressFill.style.animation = 'none';
        }
        
        // 更新瓦数和价格
        updateConfigStats();
        
        scoreElement.style.display = 'block';
    }
}

// 计算并更新配置统计信息（瓦数和价格）
function updateConfigStats() {
    const { cpu, motherboard, ram, gpu, storage, psu, case: caseComponent } = currentConfig.components;
    let totalWattage = 0;
    let totalPrice = 0;
    
    // CPU功耗和价格
    if (cpu) {
        let cpuPower = cpu.powerConsumption || cpu.tdp || 65;
        // 尝试从specs字段解析功耗信息
        if (!cpuPower || cpuPower === 65) {
            try {
                let specsObj;
                if (typeof cpu.specs === 'string') {
                    specsObj = JSON.parse(cpu.specs);
                } else {
                    specsObj = cpu.specs;
                }
                if (specsObj && (specsObj.tdp || specsObj.powerConsumption)) {
                    cpuPower = specsObj.tdp || specsObj.powerConsumption;
                    if (typeof cpuPower === 'string') {
                        cpuPower = parseInt(cpuPower.replace(/[^0-9]/g, '')) || 65;
                    }
                }
            } catch (e) {
                console.warn('Failed to parse CPU specs for power:', e);
            }
        }
        totalWattage += isNaN(cpuPower) ? 65 : Number(cpuPower);
        const cpuPrice = cpu.price || 0;
        totalPrice += isNaN(cpuPrice) ? 0 : Number(cpuPrice);
    }
    
    // 主板功耗和价格
    if (motherboard) {
        totalWattage += 30; // 主板大约30W
        const mbPrice = motherboard.price || 0;
        totalPrice += isNaN(mbPrice) ? 0 : Number(mbPrice);
    }
    
    // 内存功耗和价格
    if (ram) {
        const ramCapacity = ram.capacity || 16;
        const ramPower = (ramCapacity / 8) * 5; // 每8GB约5W
        totalWattage += isNaN(ramPower) ? 10 : Number(ramPower);
        const ramPrice = ram.price || 0;
        totalPrice += isNaN(ramPrice) ? 0 : Number(ramPrice);
    }
    
    // GPU功耗和价格
    if (gpu) {
        let gpuPower = gpu.powerConsumption || gpu.tdp || 150;
        // 尝试从specs字段解析功耗信息
        if (!gpuPower || gpuPower === 150) {
            try {
                let specsObj;
                if (typeof gpu.specs === 'string') {
                    specsObj = JSON.parse(gpu.specs);
                } else {
                    specsObj = gpu.specs;
                }
                if (specsObj && (specsObj.tdp || specsObj.powerConsumption)) {
                    gpuPower = specsObj.tdp || specsObj.powerConsumption;
                    if (typeof gpuPower === 'string') {
                        gpuPower = parseInt(gpuPower.replace(/[^0-9]/g, '')) || 150;
                    }
                }
            } catch (e) {
                console.warn('Failed to parse GPU specs for power:', e);
            }
        }
        totalWattage += isNaN(gpuPower) ? 150 : Number(gpuPower);
        const gpuPrice = gpu.price || 0;
        totalPrice += isNaN(gpuPrice) ? 0 : Number(gpuPrice);
    }
    
    // 存储功耗和价格
    if (storage && storage.length > 0) {
        storage.forEach(drive => {
            if (drive) {
                const drivePower = drive.type === 'HDD' ? 10 : 5; // HDD 10W, SSD 5W
                totalWattage += drivePower;
                const drivePrice = drive.price || 0;
                totalPrice += isNaN(drivePrice) ? 0 : Number(drivePrice);
            }
        });
    }
    
    // 电源价格（不计算功耗）
    if (psu) {
        const psuPrice = psu.price || 0;
        totalPrice += isNaN(psuPrice) ? 0 : Number(psuPrice);
    }
    
    // 机箱功耗和价格
    if (caseComponent) {
        totalWattage += 20; // 机箱风扇等约20W
        const casePrice = caseComponent.price || 0;
        totalPrice += isNaN(casePrice) ? 0 : Number(casePrice);
    }
    
    // 确保最终结果不是NaN
    totalWattage = isNaN(totalWattage) ? 0 : Math.round(totalWattage);
    totalPrice = isNaN(totalPrice) ? 0 : totalPrice;
    
    // 更新显示
    const wattageElement = document.getElementById('totalWattage');
    const priceElement = document.getElementById('totalPrice');
    
    if (wattageElement) {
        wattageElement.textContent = totalWattage + 'W';
    }
    
    if (priceElement) {
        priceElement.textContent = '¥' + totalPrice.toLocaleString();
    }
}

// 添加存储槽位
function addStorageSlot() {
    const additionalSlots = document.getElementById('additionalStorageSlots');
    const currentSlots = additionalSlots.children.length;
    const newSlotIndex = currentSlots + 1;
    
    if (newSlotIndex >= 4) { // 最多4个存储设备
        showCustomAlert('最多只能添加4个存储设备', 'warning');
        return;
    }
    
    const slotHtml = `
        <div class="component-search" id="storage-slot-${newSlotIndex}" style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #e9ecef;">
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
                <span style="font-weight: 600; color: #666;">存储设备 ${newSlotIndex + 1}</span>
                <button onclick="removeStorageSlot(${newSlotIndex})" style="margin-left: auto; background: #dc3545; color: white; border: none; border-radius: 4px; padding: 4px 8px; cursor: pointer; font-size: 12px;">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <input type="text" class="search-input" placeholder="搜索存储设备..." 
                   onkeyup="searchComponent('storage', this.value, ${newSlotIndex})" onfocus="showSearchResults('storage', ${newSlotIndex})">
            <div class="search-results" id="storage-${newSlotIndex}-results"></div>
            <div class="selected-component" id="storage-${newSlotIndex}-selected">
                <div class="component-info">
                    <div class="component-details">
                        <h4 id="storage-${newSlotIndex}-name"></h4>
                        <p id="storage-${newSlotIndex}-specs"></p>
                    </div>
                    <div>
                        <span class="component-price" id="storage-${newSlotIndex}-price"></span>
                        <button class="remove-component" onclick="removeComponent('storage', ${newSlotIndex})">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    additionalSlots.insertAdjacentHTML('beforeend', slotHtml);
}

// 移除存储槽位
function removeStorageSlot(slotIndex) {
    const slotElement = document.getElementById(`storage-slot-${slotIndex}`);
    if (slotElement) {
        // 先移除组件数据
        if (currentConfig.components.storage && currentConfig.components.storage[slotIndex]) {
            currentConfig.components.storage[slotIndex] = null;
            currentConfig.components.storage = currentConfig.components.storage.filter(item => item !== null);
        }
        
        // 移除DOM元素
        slotElement.remove();
        
        // 更新性能评分和兼容性
        updatePerformanceScore();
        checkCompatibility();
    }
}

// 创建新配置
function createNewConfig() {
    currentConfig = {
        id: null,
        title: '',
        components: {
            cpu: null,
            motherboard: null,
            ram: null,
            gpu: null,
            storage: [],
            psu: null,
            case: null
        }
    };
    
    // 清空所有选择
    ['cpu', 'motherboard', 'ram', 'gpu', 'storage', 'psu', 'case'].forEach(type => {
        const selectedElement = document.getElementById(type + '-selected');
        if (selectedElement) {
            selectedElement.classList.remove('active');
        }
        const searchInput = document.querySelector(`#${type}-results`)?.previousElementSibling;
        if (searchInput) {
            searchInput.value = '';
        }
    });
    
    // 清空额外的存储槽位
    document.getElementById('additionalStorageSlots').innerHTML = '';
    
    // 隐藏保存按钮、兼容性状态和性能评分
    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) {
        saveBtn.style.display = 'none';
    }
    const fixedSaveBtn = document.getElementById('fixedSaveBtn');
    if (fixedSaveBtn) {
        fixedSaveBtn.style.display = 'none';
    }
    document.getElementById('compatibilityStatus').style.display = 'none';
    document.getElementById('performanceScore').style.display = 'none';
    
    // 更新标题
    document.getElementById('contentTitle').textContent = '新建配置单';
    
    // 移除配置列表中的active状态
    document.querySelectorAll('.config-item').forEach(item => {
        item.classList.remove('active');
    });
    
    // 禁用滚动（没有配置时）
    const mainContentBody = document.querySelector('.main-content-body');
    if (mainContentBody) {
        mainContentBody.classList.add('no-scroll');
    }
}

// 显示自定义保存弹窗
function showSaveModal() {
    if (!hasAnyComponents()) {
        showCustomAlert('请至少选择一个组件', 'warning');
        return;
    }
    
    const modal = document.getElementById('saveModal');
    const titleInput = document.getElementById('configTitle');
    const descInput = document.getElementById('configDescription');
    
    // 如果是编辑现有配置，填入当前信息
    if (currentConfig.title) {
        titleInput.value = currentConfig.title;
        descInput.value = currentConfig.description || '';
    } else {
        titleInput.value = '';
        descInput.value = '';
    }
    
    modal.style.display = 'flex';
    modal.style.justifyContent = 'center';
    modal.style.alignItems = 'center';
    titleInput.focus();
}

// 隐藏自定义保存弹窗
function hideSaveModal() {
    document.getElementById('saveModal').style.display = 'none';
}

// 关闭保存弹窗（别名函数）
function closeSaveModal() {
    hideSaveModal();
}

// 确认保存配置（弹窗按钮调用）
function confirmSaveConfig() {
    saveConfig();
}

// 保存配置
function saveConfig() {
    const titleInput = document.getElementById('configTitle');
    const descInput = document.getElementById('configDescription');
    const title = titleInput.value.trim();
    const description = descInput.value.trim();
    if (!title) {
        showCustomAlert('请输入配置单名称', 'warning');
        return;
    }
    currentConfig.title = title;
    currentConfig.description = description;
    // 构造后端需要的UserConfig对象
    const userConfig = {
        title: currentConfig.title,
        description: currentConfig.description,
        configData: JSON.stringify({ components: currentConfig.components })
    };
    fetch('/api/user-configs', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: JSON.stringify(userConfig)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            showSuccessMessage('配置保存成功!');
            hideSaveModal();
            loadConfigList();
        } else {
            showCustomAlert(data.message || '保存失败', 'error');
        }
    })
    .catch(err => {
        showCustomAlert('保存失败: ' + err.message, 'error');
    });
}

// 显示成功消息
function showSuccessMessage(message) {
    const successDiv = document.createElement('div');
    successDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #28a745;
        color: white;
        padding: 12px 20px;
        border-radius: 6px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 10000;
        font-weight: 500;
        animation: slideInRight 0.3s ease;
    `;
    successDiv.textContent = message;
    
    document.body.appendChild(successDiv);
    
    setTimeout(() => {
        successDiv.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(successDiv);
        }, 300);
    }, 2000);
}

// 加载配置列表
function loadConfigList() {
    const configList = document.getElementById('configList');
    
    // 从后端API获取配置单列表
    return fetch('/api/user-configs', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            throw new Error(data.message || '获取配置单失败');
        }
        
        const savedConfigs = data.data || [];
        
        if (savedConfigs.length === 0) {
            configList.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-desktop"></i>
                    <p>暂无配置单</p>
                    <p>点击"新建"开始创建您的第一个配置</p>
                </div>
            `;
            return;
        }
        
        configList.innerHTML = savedConfigs.map(config => {
             const configData = JSON.parse(config.configData || '{}');
             const components = configData.components || {};
             const componentCount = Object.values(components).filter(c => c !== null && (Array.isArray(c) ? c.length > 0 : true)).length;
             let totalPrice = 0;
             Object.values(components).forEach(c => {
                 if (c !== null) {
                     if (Array.isArray(c)) {
                         c.forEach(item => {
                             if (item) totalPrice += item.price;
                         });
                     } else {
                         totalPrice += c.price;
                     }
                 }
             });
        
            return `
                <div class="config-item" data-config-id="${config.id}">
                    <div class="config-content">
                        <div class="config-title">${config.title}</div>
                        <div class="config-meta">
                            <span><i class="fas fa-microchip"></i> ${componentCount} 个组件</span>
                            <span><i class="fas fa-yen-sign"></i> ¥${totalPrice.toLocaleString()}</span>
                        </div>
                        <div class="config-description">${config.description || '暂无描述'}</div>
                    </div>
                    <div class="config-actions">
                        <button class="btn-icon edit-btn" data-config-id="${config.id}" title="编辑">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-icon btn-danger delete-btn" data-config-id="${config.id}" title="删除">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;
        }).join('');
        
        // 为配置项添加悬浮预览事件
        addPreviewEvents();
        
        // 为编辑和删除按钮添加事件
        addConfigButtonEvents();
    })
    .catch(error => {
        console.error('加载配置单失败:', error);
        configList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>加载配置单失败</p>
                <p>${error.message}</p>
            </div>
        `;
    });
}

// 全局变量存储要删除的配置ID
let configToDelete = null;

// 删除配置
function deleteConfig(configId, event) {
    event.stopPropagation(); // 阻止事件冒泡
    
    configToDelete = configId;
    showDeleteModal();
}

// 显示删除确认弹窗
function showDeleteModal() {
    const modal = document.getElementById('deleteModal');
    modal.classList.add('show');
    document.body.style.overflow = 'hidden'; // 防止背景滚动
    
    // 点击背景关闭弹窗
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            hideDeleteModal();
        }
    });
    
    // ESC键关闭弹窗
    document.addEventListener('keydown', handleEscapeKey);
}

// 处理ESC键事件
function handleEscapeKey(e) {
    if (e.key === 'Escape') {
        hideDeleteModal();
    }
}

// 隐藏删除确认弹窗
function hideDeleteModal() {
    const modal = document.getElementById('deleteModal');
    modal.classList.remove('show');
    document.body.style.overflow = 'auto';
    configToDelete = null;
    
    // 移除ESC键监听器
    document.removeEventListener('keydown', handleEscapeKey);
}

// 确认删除
function confirmDelete() {
    if (configToDelete === null) return;
    
    // 调用后端API删除配置
    fetch(`/api/user-configs/${configToDelete}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            throw new Error(data.message || '删除配置失败');
        }
        
        // 如果删除的是当前配置，创建新配置
        if (currentConfig.id === configToDelete) {
            createNewConfig();
        }
        
        loadConfigList();
        showSuccessMessage('配置删除成功!');
        hideDeleteModal();
    })
    .catch(error => {
        console.error('删除配置失败:', error);
        showCustomAlert('删除配置失败: ' + error.message, 'error');
        hideDeleteModal();
    });
}

// 配置预览悬浮窗功能
let previewTimeout;
let currentPreviewConfig = null;

// 显示配置预览
function showConfigPreview(configId, event) {
    console.log('showConfigPreview called with configId:', configId); // 调试日志
    clearTimeout(previewTimeout);
    
    // 从后端API获取配置详情
    fetch(`/api/user-configs/${configId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            console.error('获取配置预览失败:', data.message);
            return;
        }
        
        const config = data.data;
        if (!config) return;
        
        // 解析配置数据
        const configData = JSON.parse(config.configData || '{}');
        const components = configData.components || {};
        
        currentPreviewConfig = configId;
        
        // 显示预览内容
        if (event && event.currentTarget) {
            displayConfigPreview(config, components, event);
        } else {
            console.warn('Event object lost in showConfigPreview');
        }
    })
    .catch(error => {
        console.error('获取配置预览失败:', error);
    });
}

// 显示配置预览内容的辅助函数
function displayConfigPreview(config, components, event) {
    
    // 更新预览内容
    document.getElementById('previewTitle').textContent = config.title || '未命名配置';
    
    // 计算总功耗
    const totalPower = calculateTotalPower(components);
    document.getElementById('previewPower').textContent = totalPower + 'W';
    
    // 生成组件列表
    const componentsContainer = document.getElementById('previewComponents');
    componentsContainer.innerHTML = '';
    
    const componentTypes = {
        cpu: { name: 'CPU', icon: 'fas fa-microchip' },
        motherboard: { name: '主板', icon: 'fas fa-memory' },
        ram: { name: '内存', icon: 'fas fa-memory' },
        gpu: { name: '显卡', icon: 'fas fa-tv' },
        storage: { name: '存储', icon: 'fas fa-hdd' },
        psu: { name: '电源', icon: 'fas fa-bolt' },
        case: { name: '机箱', icon: 'fas fa-cube' }
    };
    
    let totalPrice = 0;
    
    Object.keys(componentTypes).forEach(type => {
        const component = components[type];
        if (component && (type !== 'storage' || component.length > 0)) {
            const item = document.createElement('div');
            item.className = 'config-preview-item';
            
            let componentName = '未选择';
            let componentPrice = 0;
            
            if (type === 'storage' && Array.isArray(component)) {
                if (component.length > 0) {
                    componentName = component.map(s => s.name).join(', ');
                    componentPrice = component.reduce((sum, s) => sum + s.price, 0);
                }
            } else if (component) {
                componentName = component.name || '未知';
                componentPrice = component.price || 0;
            }
            
            totalPrice += componentPrice;
            
            item.innerHTML = `
                <div class="config-preview-component">
                    <i class="${componentTypes[type].icon} config-preview-icon"></i>
                    <span class="config-preview-name">${componentTypes[type].name}</span>
                </div>
                <span class="config-preview-value">${componentName}</span>
            `;
            
            componentsContainer.appendChild(item);
        }
    });
    
    // 更新总价
    document.getElementById('previewTotalPrice').textContent = '¥' + totalPrice.toLocaleString();
    
    // 定位悬浮窗
    const tooltip = document.getElementById('configPreviewTooltip');
    if (!tooltip) {
        console.error('Tooltip element not found');
        return;
    }
    
    if (!event || !event.currentTarget) {
        console.error('Event or currentTarget is null');
        return;
    }
    
    const rect = event.currentTarget.getBoundingClientRect();
    if (!rect) {
        console.error('Could not get bounding rect');
        return;
    }
    
    // 使用fixed定位，相对于视口计算位置
    let left = rect.right + 10;
    let top = rect.top;
    
    // 确保悬浮窗不超出视口右边界
    const tooltipWidth = 380; // 最大宽度
    if (left + tooltipWidth > window.innerWidth) {
        left = rect.left - tooltipWidth - 10;
    }
    
    // 确保悬浮窗不超出视口下边界
    const tooltipHeight = 300; // 估计高度
    if (top + tooltipHeight > window.innerHeight) {
        top = window.innerHeight - tooltipHeight - 10;
    }
    
    // 确保悬浮窗不超出视口上边界
    if (top < 10) {
        top = 10;
    }
    
    tooltip.style.left = left + 'px';
    tooltip.style.top = top + 'px';
    
    // 显示悬浮窗
    console.log('Adding show class to tooltip'); // 调试日志
    tooltip.classList.add('show');
    // 确保tooltip可见
    tooltip.style.display = 'block';
    tooltip.style.opacity = '1';
    tooltip.style.visibility = 'visible';
    console.log('Tooltip classes:', tooltip.className); // 调试日志
}

// 隐藏配置预览
function hideConfigPreview() {
    previewTimeout = setTimeout(() => {
        const tooltip = document.getElementById('configPreviewTooltip');
        tooltip.classList.remove('show');
        // 确保tooltip隐藏
        tooltip.style.display = 'none';
        tooltip.style.opacity = '0';
        tooltip.style.visibility = 'hidden';
        currentPreviewConfig = null;
    }, 100);
}

// 计算总功耗
function calculateTotalPower(components) {
    let totalPower = 0;
    
    // 从specs字段解析功耗信息的辅助函数
    function getPowerFromSpecs(component, defaultPower) {
        // 首先尝试直接获取功耗字段
        let power = component.powerConsumption || component.power || component.tdp;
        
        // 如果没有直接字段，尝试从specs JSON中解析
        if (!power && component.specs) {
            try {
                let specsObj;
                if (typeof component.specs === 'string') {
                    specsObj = JSON.parse(component.specs);
                } else {
                    specsObj = component.specs;
                }
                
                // 查找功耗相关字段
                for (const [key, value] of Object.entries(specsObj)) {
                    const keyLower = key.toLowerCase();
                    if (keyLower.includes('power') || keyLower.includes('功耗') || keyLower.includes('tdp')) {
                        const powerMatch = String(value).match(/(\d+)\s*w/i);
                        if (powerMatch) {
                            power = parseInt(powerMatch[1]);
                            break;
                        }
                    }
                }
            } catch (e) {
                console.warn('Failed to parse specs for power calculation:', e);
            }
        }
        
        // 如果是字符串格式（如"125W"），提取数字部分
        if (typeof power === 'string') {
            const powerMatch = power.match(/(\d+)\s*w/i);
            power = powerMatch ? parseInt(powerMatch[1]) : defaultPower;
        }
        
        return isNaN(power) || !power ? defaultPower : Number(power);
    }
    
    // CPU功耗
    if (components.cpu) {
        totalPower += getPowerFromSpecs(components.cpu, 65);
    }
    
    // GPU功耗
    if (components.gpu) {
        totalPower += getPowerFromSpecs(components.gpu, 150);
    }
    
    // 主板功耗
    if (components.motherboard) {
        totalPower += 30; // 主板约30W
    }
    
    // 内存功耗
    if (components.ram) {
        const ramCapacity = components.ram.capacity || 16;
        const ramCount = Math.ceil(ramCapacity / 8);
        totalPower += ramCount * 3; // 每条内存约3W
    }
    
    // 存储功耗
    if (components.storage && Array.isArray(components.storage)) {
        components.storage.forEach(storage => {
            if (storage && storage.type) {
                if (storage.type === 'SSD') {
                    totalPower += 5; // SSD约5W
                } else {
                    totalPower += 10; // HDD约10W
                }
            }
        });
    }
    
    // 其他组件功耗
    totalPower += 20; // 风扇、其他组件约20W
    
    // 确保结果不是NaN
    totalPower = isNaN(totalPower) ? 0 : totalPower;
    
    return Math.round(totalPower);
}

// 为配置项添加悬浮预览事件
function addPreviewEvents() {
    document.querySelectorAll('.config-item').forEach(item => {
        // 移除旧的事件监听器
        item.removeEventListener('mouseenter', handleMouseEnter);
        item.removeEventListener('mouseleave', handleMouseLeave);
        item.removeEventListener('click', handleConfigClick);
        
        // 添加新的事件监听器
        item.addEventListener('mouseenter', handleMouseEnter);
        item.addEventListener('mouseleave', handleMouseLeave);
        item.addEventListener('click', handleConfigClick);
    });
}

function handleMouseEnter(e) {
    const configId = this.getAttribute('data-config-id');
    console.log('Mouse enter on config:', configId); // 调试日志
    if (configId) {
        // 确保事件对象有效，使用this作为currentTarget的备选
        const event = e || window.event;
        if (event) {
            // 如果event.currentTarget为空，使用this作为currentTarget
            if (!event.currentTarget) {
                event.currentTarget = this;
            }
            showConfigPreview(configId, event);
        } else {
            // 如果没有事件对象，创建一个模拟的事件对象
            const mockEvent = {
                currentTarget: this,
                target: this
            };
            showConfigPreview(configId, mockEvent);
        }
    }
}

function handleMouseLeave() {
    hideConfigPreview();
}

function handleConfigClick(e) {
     const configId = this.getAttribute('data-config-id');
     
     // 检查是否点击了按钮
     if (e.target.closest('.edit-btn')) {
         e.preventDefault();
         e.stopPropagation();
         loadConfig(parseInt(configId));
         return;
     }
     
     if (e.target.closest('.delete-btn')) {
         e.preventDefault();
         e.stopPropagation();
         deleteConfig(parseInt(configId), e);
         return;
     }
     
     // 如果不是点击按钮，则加载配置
     if (!e.target.closest('.config-actions')) {
         loadConfig(parseInt(configId));
     }
 }

// 加载配置
function loadConfig(configId) {
    // 从后端API获取配置详情
    fetch(`/api/user-configs/${configId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (!data.success) {
            throw new Error(data.message || '获取配置失败');
        }
        
        const config = data.data;
        if (!config) {
            console.error('配置不存在');
            return;
        }
        
        // 解析配置数据
        const configData = JSON.parse(config.configData || '{}');
        currentConfig = {
            id: config.id,
            title: config.title,
            description: config.description,
            components: configData.components || {}
        };
        
        // 应用配置到UI
        applyConfigToUI(currentConfig);
    })
    .catch(error => {
        console.error('加载配置失败:', error);
        showCustomAlert('加载配置失败: ' + error.message, 'error');
    });
}

// 将配置应用到UI的辅助函数
function applyConfigToUI(config) {
    
    // 清空所有现有选择
    ['cpu', 'motherboard', 'ram', 'gpu', 'storage', 'psu', 'case'].forEach(type => {
        const selectedElement = document.getElementById(type + '-selected');
        if (selectedElement) {
            selectedElement.classList.remove('active');
        }
        const searchInput = document.querySelector(`#${type}-results`)?.previousElementSibling;
        if (searchInput) {
            searchInput.value = '';
        }
    });
    
    // 清空额外的存储槽位
    document.getElementById('additionalStorageSlots').innerHTML = '';
    
    // 更新UI显示所有组件
    Object.keys(config.components).forEach(type => {
        const component = config.components[type];
        if (type === 'storage' && Array.isArray(component)) {
            // 处理存储设备数组
            component.forEach((storageItem, index) => {
                if (storageItem) {
                    if (index > 0) {
                        // 额外的存储设备需要先创建槽位
                        addStorageSlot();
                    }
                    selectComponent('storage', storageItem.id, index);
                }
            });
        } else if (component) {
            selectComponent(type, component.id);
        }
    });
    
    // 更新标题和按钮
    document.getElementById('contentTitle').textContent = config.title;
    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) {
        saveBtn.style.display = 'block';
    }
    const fixedSaveBtn = document.getElementById('fixedSaveBtn');
    if (fixedSaveBtn) {
        fixedSaveBtn.style.display = 'flex';
    }
    
    // 更新配置列表中的active状态
    document.querySelectorAll('.config-item').forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('data-config-id') == config.id) {
            item.classList.add('active');
        }
    });
    
    // 检查兼容性和更新性能评分
    checkCompatibility();
    updatePerformanceScore();
}

// 为配置卡片中的编辑和删除按钮添加事件
function addConfigButtonEvents() {
    // 为编辑按钮添加点击事件
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.onclick = function(event) {
            event.stopPropagation(); // 阻止事件冒泡，防止触发配置卡片的点击事件
            const configId = this.getAttribute('data-config-id');
            if (configId) {
                loadConfig(configId);
            }
        };
    });
    
    // 为删除按钮添加点击事件
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.onclick = function(event) {
            event.stopPropagation(); // 阻止事件冒泡，防止触发配置卡片的点击事件
            const configId = this.getAttribute('data-config-id');
            if (configId) {
                deleteConfig(configId, event);
            }
        };
    });
}

// 点击页面其他地方隐藏搜索结果
document.addEventListener('click', function(event) {
    if (!event.target.closest('.component-search')) {
        hideAllSearchResults();
    }
});

// 防止搜索结果框内的点击事件冒泡
document.addEventListener('click', function(event) {
    if (event.target.closest('.search-results')) {
        event.stopPropagation();
    }
});