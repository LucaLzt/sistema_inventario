document.getElementById('modalViewUser').addEventListener('show.bs.modal', function (event) {
    var button = event.relatedTarget;
    if (!button) return;
	
	// User's Elements
    document.getElementById('view-id').value = button.getAttribute('data-id') || '';
    document.getElementById('view-email').value = button.getAttribute('data-email') || '';
    document.getElementById('view-fullname').value = button.getAttribute('data-fullname') || '';
    document.getElementById('view-mobile').value = button.getAttribute('data-mobile') || '';
    document.getElementById('view-role').value = button.getAttribute('data-role') || '';
    document.getElementById('view-registeredat').value = button.getAttribute('data-registeredat') || '';
    document.getElementById('view-updatedat').value = button.getAttribute('data-updatedat') || '';
	document.getElementById('view-active').value = button.getAttribute('data-active') === 'true' ? "Yes" : "No";
	document.getElementById('view-active-hidden').value = button.getAttribute('data-active');
	
	// Elements through polymorphism
	document.getElementById('view-branchid').value = button.getAttribute('data-branchid') || '';
	document.getElementById('view-superadmin').checked = button.getAttribute('data-superadmin') === 'true';
	
	// Show/Hide Branch ID and Super Admin fields
	var role = button.getAttribute('data-role');
	if (role === 'EMPLOYEE') {
		document.getElementById('branchid-group').style.display = 'block';
        document.getElementById('superadmin-group').style.display = 'none';
	} else if (role === 'ADMIN') {
		document.getElementById('branchid-group').style.display = 'none';
        document.getElementById('superadmin-group').style.display = 'block';
    } else {
		document.getElementById('branchid-group').style.display = 'none';
        document.getElementById('superadmin-group').style.display = 'none';
    }
	
	// Select the branch in the select
	var branchId = button.getAttribute('data-branchid');
	var branchSelect = document.getElementById('view-branchid');
	if (branchId) {
	    branchSelect.value = branchId;
	} else {
	    branchSelect.value = "";
	}
	
});