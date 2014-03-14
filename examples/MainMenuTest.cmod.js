EventHandler.bindEvent("Comcraft.displayScreen", function(screen) {
	if (screen.isGui("MainMenu")) {
		console.info("Main Menu")
		screen.initGui = function() {
			centerX = (this.cc.screenWidth - GuiButton.prototype.buttonWidth()) / 2;
			this.elementsList.push(new GuiButton(7, centerX, this.calcBtnY(-1), "Test"))
		}
		screen.addGuiActionHandler(function(guiButton) {
			if (guiButton.getId() == 7) {
				console.debug("Test was clicked")
			}
		})
	}
})
