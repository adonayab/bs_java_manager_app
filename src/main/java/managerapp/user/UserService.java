package managerapp.user;

import managerapp.models.User;
import managerapp.models.forms.UserForm;


public interface UserService {

    public User save(UserForm userForm) throws EmailExistsException;
    public User findByEmail(String email);

}