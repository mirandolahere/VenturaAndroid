package com.application.venturaapp.fitosanitario.listener;

import com.application.venturaapp.fitosanitario.entity.FitosanitarioDetalleResponse;
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse;

public interface FitosanitarioDetalleItemListener {

    void laborItemClickListener(FitosanitarioDetalleResponse position);
    void laborItemUpdateClickListener(FitosanitarioDetalleResponse position);
    void laborItemDeletelickListener(FitosanitarioDetalleResponse position);

}
