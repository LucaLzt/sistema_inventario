document.getElementById('modalEditProduct').addEventListener('show.bs.modal', function (event) {
	var button = event.relatedTarget;
	if (!button) return;
	
	document.getElementById('edit-id').value = button.getAttribute('data-id') || '';
	document.getElementById('edit-name').value = button.getAttribute('data-name') || '';
	document.getElementById('edit-code').value = button.getAttribute('data-code') || '';
	document.getElementById('edit-description').value = button.getAttribute('data-description') || '';
	document.getElementById('edit-priceunit').value = button.getAttribute('data-priceunit') || '';
	document.getElementById('edit-sellingpercentage').value = button.getAttribute('data-sellingpercentage') || '';
	document.getElementById('edit-stockminimum').value = button.getAttribute('data-stockminimum') || '';
	document.getElementById('edit-categoryid').value = button.getAttribute('data-categoryid') || '';
	document.getElementById('edit-active').checked = button.getAttribute('data-active') === 'true';
});