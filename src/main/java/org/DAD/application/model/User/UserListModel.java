package org.DAD.application.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserListModel {
    public List<UserOtherModel> users;
}
