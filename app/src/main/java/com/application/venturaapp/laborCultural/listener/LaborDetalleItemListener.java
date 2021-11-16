package com.application.venturaapp.laborCultural.listener;

import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse;
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse;

public interface LaborDetalleItemListener {

    void laborItemClickListener(LaborCulturalDetalleResponse position);
    void laborItemUpdateClickListener(LaborCulturalDetalleResponse position);
    void laborItemDeletelickListener(LaborCulturalDetalleResponse position);

}
