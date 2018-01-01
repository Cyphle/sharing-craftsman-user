package fr.sharingcraftsman.user.domain.authorization;

import java.util.List;

public interface RoleAdministrator {
  List<Role> getRolesOf(String group);
}
