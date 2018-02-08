package edu.utdallas.utdesign.teach4service.resources.responses;


import com.google.common.collect.Lists;
import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse
{
    private List<Sessions> sessions = Lists.newArrayList();
}
