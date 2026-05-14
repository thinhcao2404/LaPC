
function addVariantRow() {
    const tbody = document.getElementById('variants-body');
    const tr = document.createElement('tr');

    attrCounts[variantIndex] = 0;

    tr.innerHTML = `
        <td><input type="text" name="variants[${variantIndex}].sku" class="form-control" placeholder="Mã SKU" required></td>
        <td>
            <div id="attrs-${variantIndex}"></div>
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
    variantIndex++;
}

function addAttribute(vIndex) {
    const container = document.getElementById('attrs-' + vIndex);
    const aIndex = attrCounts[vIndex];

    const div = document.createElement('div');
    div.className = 'd-flex mb-1';
    div.innerHTML = `
        <input type="text" name="variants[${vIndex}].attributes[${aIndex}].name" class="form-control form-control-sm me-1" placeholder="Tên (VD: RAM)">
        <input type="text" name="variants[${vIndex}].attributes[${aIndex}].value" class="form-control form-control-sm" placeholder="Giá trị (VD: 16GB)">
    `;
    container.appendChild(div);
    attrCounts[vIndex]++;
}