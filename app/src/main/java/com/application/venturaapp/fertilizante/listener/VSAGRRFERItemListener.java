package com.application.venturaapp.fertilizante.listener;

import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS;
import com.application.venturaapp.fitosanitario.entity.VS_AGR_DSCOCollection;

public interface VSAGRRFERItemListener {

    void laborItemClickListener(VS_AGR_DSCOCollection position, VSAGRRCOS vsagrrcos);

    void laborUpdateClickListener(VS_AGR_DSCOCollection position, VSAGRRCOS vsagrrcos);

    void laborDeleteClickListener(VS_AGR_DSCOCollection position, VSAGRRCOS vsagrrcos);

}
