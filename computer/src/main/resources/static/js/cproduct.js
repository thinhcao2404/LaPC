let variantIndex = 1;
let attrCounts = { 0: 1 };

function addVariantRow() {
    const tbody = document.getElementById('variants-body');
    const tr = document.createElement('tr');

    // Gán mặc định dòng cấu hình mới đẻ ra đã có sẵn 1 thuộc tính đầu tiên (index 0)
    attrCounts[variantIndex] = 1;

    // QUAN TRỌNG: Bắt buộc phải dùng dấu Backtick ( ` ) phím nằm dưới nút ESC để bọc chuỗi HTML thì biến ${} mới hoạt động.
    tr.innerHTML = `
        <td><input type="text" name="variants[${variantIndex}].sku" class="form-control" placeholder="Mã SKU" required></td>
        <td>
            <div id="attrs-${variantIndex}">
                <div class="d-flex mb-1">
                    <input type="text" name="variants[${variantIndex}].attributes[0].name" class="form-control form-control-sm me-1" placeholder="Tên (VD: Màu)">
                    <input type="text" name="variants[${variantIndex}].attributes[0].value" class="form-control form-control-sm" placeholder="Giá trị (VD: Đen)">
                </div>
            </div>
            
            <button type="button" class="btn btn-sm btn-outline-secondary py-0 mt-1" onclick="addAttribute(${variantIndex})">
                <i class="fa-solid fa-plus me-1"></i>Thêm thuộc tính
            </button>
        </td>
        <td><input type="number" name="variants[${variantIndex}].price" class="form-control" required></td>
        <td><input type="number" name="variants[${variantIndex}].stock" class="form-control"></td>
        <td>
            <button type="button" class="btn btn-danger btn-sm" onclick="this.closest('tr').remove()">
                <i class="fa-solid fa-trash-can"></i>
            </button>
        </td>
    `;
    tbody.appendChild(tr);
    variantIndex++; // Đẻ xong thì tăng biến đếm lên cho dòng sau
}

function addAttribute(vIndex) {
    const container = document.getElementById('attrs-' + vIndex);
    const aIndex = attrCounts[vIndex];

    const div = document.createElement('div');
    div.className = 'd-flex mb-1';

    // Ở hàm này, vIndex là số thứ tự của Cấu hình, aIndex là số thứ tự của Thuộc tính
    div.innerHTML = `
        <input type="text" name="variants[${vIndex}].attributes[${aIndex}].name" class="form-control form-control-sm me-1" placeholder="Tên">
        <input type="text" name="variants[${vIndex}].attributes[${aIndex}].value" class="form-control form-control-sm" placeholder="Giá trị">
    `;
    container.appendChild(div);

    attrCounts[vIndex]++;
}