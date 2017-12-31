package fr.sharingcraftsman.user.domain.ports.admin;

import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;

import java.util.List;

public interface CompanyAdmin {
  List<AdminCollaborator> getAllCollaborators();
}
