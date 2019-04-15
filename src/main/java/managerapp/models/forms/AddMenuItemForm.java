package managerapp.models.forms;

import managerapp.models.Message;
import managerapp.models.Menu;

import javax.validation.constraints.NotNull;

public class AddMenuItemForm {

    private Menu menu;
    private Iterable<Message> cheeses;

    @NotNull
    private int menuId;

    @NotNull
    private int cheeseId;

    public AddMenuItemForm() { }

    public AddMenuItemForm(Menu menu, Iterable<Message> cheeses) {
        this.menu = menu;
        this.cheeses = cheeses;
    }

    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Iterable<Message> getCheeses() {
        return cheeses;
    }
    public void setCheeses(Iterable<Message> cheeses) {
        this.cheeses = cheeses;
    }

    public int getMenuId() {
        return menuId;
    }
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getCheeseId() {
        return cheeseId;
    }
    public void setCheeseId(int cheeseId) {
        this.cheeseId = cheeseId;
    }
}
