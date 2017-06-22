
-- 3D样式POI
local function SetPolygonStyle_3D(facecolor, styleheight, linecolor, linewidth, face_on_bottom, alpha, align)
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                enable_alpha = alpha or false,
                color = facecolor or '0xFFF9F5BA',
            },
            outline = {
                color = linecolor or '0xFF999999',
                width = linewidth or 0.05,
                enable_alpha = alpha or false,
                alignment = align or 'AlignRight',
            },
        },
        
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = face_on_bottom or false, --为false时 height才有效
            height = styleheight or 3,
            face = {
                color = facecolor or '0xFFF9F5BA',
                enable_alpha = alpha or false,
            },
            outline = {
                color = linecolor or '0xFF999999',
                width = linewidth or 0.05,
                height = styleheight or 3,
                enable_alpha = alpha or false,
                enable_edge_shadow = true,
                alignment = align or 'AlignRight',
            },
        },
    }
end

local function Set3dColorWith(color, widthColor,topcolor,sidecolor,temph)
    
    if (temph == 0)
        then
        temph = 10.0
    end
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                enable_alpha = false,
                color = color,
            },
            outline = {
                color = widthColor,--内部边框
                width = 0.05,
                enable_alpha = false,
                enable_width = false,
                alignment = 'AlignRight',
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = false, --为false时 height才有效
            height = 3.0, --如果多边形有面的话，要和outline的高度相同
            face = {
                color = color,
                enable_alpha = false
            },
            outline = {
                color = widthColor,
                width = 0.1,
                height = temph,
                enable_alpha=false,
                enable_width=false,
                alignment = 'AlignRight',
                
            }
        }
    }
end

local function LINE()
    return {
        ['2d'] = {
            style = 'linestring',
            color = '0xAA000000', -- 颜色
            -- color = '0xFF0000FF', -- 颜色
            --color = '0xAA009bff', -- 颜色
            width = 1, -- 线宽
            line_style = 'NONE', -- 线型，NONE、ARROW、DASHED
            has_arrow = true, -- 是否绘制方向指示箭头，仅在line_style为NONE时有效
            has_start = true, -- 绘制起始点
            has_end = true, -- 绘制终点
            enable_alpha = true,
            -- automatic_scale = true,
            alignment = 'AlignCenter',
            -- enable_width = false,
        },
    }
end

local function Set3dColorWith111(color, widthColor,topcolor,sidecolor,temph)
    
    if (temph == 0)
        then
        temph = 10.0
    end
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                enable_alpha = false,
                color = color,
            },
            outline = {
                color = sidecolor,--内部边框
                width = 0.05,
                enable_alpha = false,
                enable_width = false,
                alignment = 'AlignRight',
                
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = false, --为false时 height才有效
            height = 3.0, --如果多边形有面的话，要和outline的高度相同
            face = {
                color = topcolor,
                enable_alpha = false
            },
            outline = {
                color = '0xffab893e',
                width = 0.1,
                height = temph,
                enable_alpha=false,
                enable_width=true,
                alignment = 'AlignRight',
                
                --   enable_edge_shadow = false,
                left_side = {
                    color = sidecolor,
                    enable_alpha = false
                },
                
                right_side = {
                    color = sidecolor,
                    enable_alpha = false
                },
                top_side = {
                    color = sidecolor,
                    enable_alpha = false
                }
                
            }
        }
    }
end

local function Set2dColorWith(color, widthColor,leftcolor,rightcolor,height)
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                enable_alpha = false,
                color = color,
            },
            outline = {
                color = widthColor,
                width = 0.05,
                enable_alpha = false,
            },
            left_side = {}
        },
    }
end

local function SetImageWith( imageName,needTiled ,angle, needAligment)
    -- body
    return{
        ['2d'] = {
            style = 'polygon',
            face = {
                
                enable_alpha = false,
                texture = imageName,
                automatic_scale = false,
                texture_rotation = angle,
                edge_aligment =  needAligment,
            },
            outline = {
                color = '0xffababab',
                width = 0.03,
                enable_alpha = false,
            },
            left_side = {}
        }
        
        
    }
end

local function DEFAULT_STYLE()
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                --color = '0xffffffab',--默认颜色
                --color = '0xfff9faff',--默认颜色
                color = '0xfff4eee1',
                enable_alpha = false,
                texture = null,
                automatic_scale = null
            },
            outline = {
                --color = '0xff8a8aae',
                color = '0xffab893e',
                width = 0.05,
                enable_alpha = false,
                enable_width = false,
                alignment = 'AlignLeft',
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = false, --为false时 height才有效
            height = 3.0, --如果多边形有面的话，要和outline的高度相同
            face = {
                color = '0xfff4eee1',
                enable_alpha = false
            },
            outline = {
                color = '0xffab893e',
                width = 0.1,
                height = temph,
                enable_alpha=false,
                enable_width=false,
                alignment = 'AlignLeft',
                --   enable_edge_shadow = false,
                
            }
        }
    }
end

local function DEFAULT_3D_STYLE()
    return{
        ['2d'] = {
            style = 'polygon',
            face = {
                color = '0xffe1e9ef',
                enable_alpha = false,
                texture = null,
                automatic_scale = null
            },
            outline = {
                color = '0xffc0c0c0',
                width = 0.12,
                enable_alpha = false,
                enable_width=true,
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = false, --为false时 height才有效
            height = 2, --如果多边形有面的话，要和outline的高度相同
            face = {
                color = '0Xfffff5ee',
                --color = '0xffe1e9ef',
                enable_alpha = false
            },
            outline = {
                color = '0XFF000000',
                width = 0.15,
                height = 2,
                enable_alpha=false,
                enable_width=true,
                left_side = {
                    color = '0XFFeed3c1',
                    enable_alpha = false
                },
                right_side = {
                    color = '0XFFeed3c1',
                    enable_alpha = false
                },
                top_side = {
                    color = '0XFF000000',
                    enable_alpha = false
                }
            }
        }
    }
end

local function GET_ICON_CACHE_PATH()
    
    local engine = GetEngine()
    local properties = engine.properties
    local os = properties["OS"]
    local icon_path = properties["iconCachePath"]
    
    if os == "iOS" or "iPhone OS"  then
        return icon_path
        
    end

end

local function GET_FONT_PATH()
    local engine = GetEngine()
    local properties = engine.properties
    local os = properties["os"]
    if os then
        if os >= "iOS/7.0" and os < "iOS/8.0" then
            return "/System/Library/Fonts/Cache/STHeiti-Light.ttc"
        elseif os >= "iOS/8.0" and os < "iOS/9.0" then
            return "/System/Library/Fonts/Core/STHeiti-Light.ttc"
        elseif os >= "iOS/9.0" then
            return "/System/Library/Fonts/LanguageSupport/PingFang.ttc"
        else
            return "/System/Library/Fonts/LanguageSupport/PingFang.ttc"
        end
    else
        return properties["lua_path"] .. "/DroidSansFallback.ttf"
    end
end

local function DEFAULT_3D_STYLE(a,height)
    return{
        ['2d'] = {
            style = 'polygon',
            face = {
                color = '0xffe1e9ef',
                enable_alpha = false,
                texture = null,
                automatic_scale = null
            },
            outline = {
                color = '0xffc0c0c0',
                width = 0.02,
                enable_alpha = false,
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            face_on_bottom = false, --为false时 height才有效
            height = d, --如果多边形有面的话，要和outline的高度相同
            face = {
                --color = '0xffe1e9ef',
                color = '0Xfffff5ee',
                enable_alpha = false
            },
            outline = {
                color = '0XFF000000',
                width = 0.05,
                height = height,
                enable_alpha=false,
                left_side = {
                    color = '0XFFeed3c1',
                    --color = '0xffe1e9ef',
                    
                    enable_alpha = false
                },
                right_side = {
                    color = '0XFFeed3c1',
                    --color = '0xffe1e9ef',
                    enable_alpha = false
                },
                top_side = {
                    color = '0XFFffffff',
                    enable_alpha = false
                }
            }
        }
    }
end

local function COLOR_STYLE(a, b, c)
    style = DEFAULT_STYLE()
    style['2d'].face.color = a;
    style['2d'].face.enable_alpha = false;
    style['2d'].outline.color = b or '0xFFc0c0c0';
    style['2d'].outline.width = c or 0.02;
    style['2d'].outline.enable_alpha = false;
    return style
end

local function COLOR_3D_STYLE(a,b,c,height)
    style = DEFAULT_3D_STYLE(a,height)
    --style = DEFAULT_3D_STYLE()
    style['2d'].face.color = a;
    style['2d'].face.enable_alpha = false;
    style['2d'].outline.color = b or '0xFFc0c0c0';
    style['2d'].outline.width = c or 0.02;
    style['2d'].outline.enable_alpha = false;
    return style
end

local function TEXTURE_1_STYLE(a, b, c)
    style = DEFAULT_STYLE()
    style['2d'].face.color = null;
    style['2d'].face.enable_alpha = true;
    style['2d'].face.texture = a;
    style['2d'].outline.color = b or '0xff7D7D7D';
    style['2d'].face.automatic_scale = true;
    style['2d'].outline.width = c or 0.1;
    return style
end

local function TEXTURE_2_STYLE(a, b, c)
    style = DEFAULT_STYLE()
    style['2d'].face.color = null;
    style['2d'].face.enable_alpha = true;
    style['2d'].face.texture = a;
    style['2d'].outline.color = b or '0xff7D7D7D';
    style['2d'].face.automatic_scale = false;
    style['2d'].outline.width = c or 0.1;
    return style
end

local function DEFAULT_ICON()
    return {
        ['2d'] = {
            style = 'icon',
            --icon = "icons/00000000.png",
            icon_url = 'http://api.ipalmap.com/logo/32/',
            icon_cache = GET_ICON_CACHE_PATH(),
            icon_online = 'logo',
            use_texture_origin_size = false,
            width = 32,
            height = 32,
            anchor_x = 0.5,
            anchor_y = 0.5
            
        }
    }
end

local function ICON(a)
    return {
        ['2d'] = {
            style = 'icon',
            icon = a,
            use_texture_origin_size = false,
            width = 32,
            height = 32,
            anchor_x = 0.5,
            anchor_y = 0.5
        }
    }
end

CONFIG = {
    views = {
        style999 = {
            layers = {
                Frame = {
                    --height_offset = 0.1,
                    renderer = {
                        type = 'simple',
                        ['2d'] = {
                            style = 'polygon',
                            face = {
                                color = '0xfffffdf8',--路
                                enable_alpha = false,
                            },
                            outline = {
                                --color = '0xff000000',
                                color = '0xffab893e',--外部边框
                                width = 1,
                                enable_alpha = false,

                                alignment = 'AlignLeft', -- 多边形外框线对齐方式设置, 取值为:'AlignLeft'、'AlignCenter'、'AlignRight',沿顺时针方向分别表示居左(外)、居中、居右(内)对齐
                                enable_width=true,
                            },
                            left_side = {}
                        },
                    }
                },
                Area = {
                    height_offset = 0,
                    renderer = {
                        type = 'unique',
                        key = {
                            'id',
                            'category',
                        },
                        default =  SetPolygonStyle_3D('0xffE5E3FC', 5,'0xffB6B5C9'),
                        updatestyles = {
                            [1] = {
                                ['2d'] = {
                                    style = 'icon',
                                    icon = 'mapicon.png',
                                    use_texture_origin_size = false,
                                    width = 32,
                                    height = 32,
                                    anchor_x = 0.5,
                                    anchor_y = 0.5
                                }
                            },
                            [2] = Set3dColorWith111('0xffff0000','0xffff0000','0xffff0000','0xffff0000',3.0),
                        },
                        styles = {
                            [23063000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            [23043000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            [23041000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            [13093000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13075000] = SetPolygonStyle_3D('0xffF9DEDE', 5,'0xffC7B1B2'),
                            [13074000] = SetPolygonStyle_3D('0xffF9DEDE', 5,'0xffC7B1B2'),
                            [13071000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13061000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13031000] = SetPolygonStyle_3D('0xffF9DEDE', 5,'0xffC7B1B2'),
                            [13003000] = SetPolygonStyle_3D('0xffFDEDD1', 5,'0xffC9BDA6'),
                            [13002000] = SetPolygonStyle_3D('0xffFDEDD1', 5,'0xffC9BDA6'),
                            [11452000] = SetPolygonStyle_3D('0xffFFDCC0', 5,'0xffCCB09A'),
                            [11471000] = SetPolygonStyle_3D('0xffFFDCC0', 5,'0xffCCB09A'),
                            [13010000] = SetPolygonStyle_3D('0xffF9DEDE', 5,'0xffC7B1B2'),
                            [13011000] = SetPolygonStyle_3D('0xffFDEDD1', 5,'0xffC9BDA6'),
                            [13036000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13062000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13063000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [13141000] = SetPolygonStyle_3D('0xffF9DEDE', 5,'0xffC7B1B2'),
                            [15000000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [15009000] = SetPolygonStyle_3D('0xffFCF295', 5,'0xffC9C177'),
                            [24091000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            [24093000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            [24097000] = SetPolygonStyle_3D('0xffCDF1F9', 5,'0xffA4C1C7'),
                            -- [962248]=Set3dColorWith('0xffff0000','0xffff0000','0xffff0000','0xffff0000',3.0),
                            [23999000]=Set3dColorWith('0xffEaEaEa','0xffab893e','0xffab893e','0xffab893e',3.0),
                            [23062000]=Set3dColorWith('0xffffffff','0xffab893e','0xffab893e','0xffab893e',3.0),
                            -- Set3dColorWith(color, widthColor,topcolor,sidecolor,temph)

                        }
                    }
                },
                Area_text = {
                    collision_detection = true,
                    font_path = GET_FONT_PATH(),
                    renderer = {
                        type = 'simple',
                        
                        ['2d'] = {
                            style = 'annotation',
                            color = '0xFF000000',
                            field = 'display',
                            size = 5,
                            unit = 'pt',
                            --height = 3.5,
                            --outline_color = '0xAAEFE4B0',
                            outline_color = '0xAAFFFFFF',
                            outline_width = 1,
                            anchor_x = 1.0,
                            anchor_y = 1.0,
                            aabbox_extend = 5,
                            
                            --anchor_style = {
                            --style = 'color_point',  —一个显示颜色点的样式
                            --color = '0xFF006699', —颜色
                            --size = 1, —大小
                            --enable_alpha = true,
                            --}
                            
                            anchor_style = {
                                style = 'icon',
                              --  icon = "icons/25133.png", —- 只要配置了当前属性，就加载本地图片
                              -- icon_url = 'http://10.0.10.150/logo/25/',
                                icon_url = 'http://api.ipalmap.com/logo/32/',
                                icon_cache = GET_ICON_CACHE_PATH(),
                                icon_online = 'logo',
                                anchor_x = 0.5,
                                anchor_y = 0.5,
                                use_texture_origin_size = false,
                                width = 32,
                                height = 32,
                            }
                            
                        }
                    }
                },
                Facility = {
                            height_offset = -0.2;
                            collision_detection = true,
                            renderer = {
                                type = 'unique',
                                key = {
                            'category'
                        },
                        default = DEFAULT_ICON(),
                       
                        updatestyles = {
                            [1] = {
                                ['2d'] = {
                                    style = 'icon',
                                    icon = 'mapicon.png',
                                    use_texture_origin_size = false,
                                    width = 32,
                                    height = 32,
                                    anchor_x = 0.5,
                                    anchor_y = 0.5
                                }
                            },
                            [2] = Set3dColorWith('0xffff0000','0xffff0000','0xffff0000','0xffff0000',3.0)
                        },
                    }
                },
                navigate = { -- 导航图层参考样式设置
                    -- height_offset = -10.2,
                    height_offset = -1,
                    renderer = {
                        type = 'unique',
                        key = {
                            'navi_name', -- 经停点默认使用这个字段区别导航线和经停点
                        },
                        default = {
                            ['2d'] = {
                                style = 'linestring',
                                color = '0xFF64b5f6', -- 颜色
                                -- color = '0xFF0000FF', -- 颜色
                                --color = '0xAA009bff', -- 颜色
                                width = 1, -- 线宽
                                line_style = 'NONE', -- 线型，NONE、ARROW、DASHED
                                has_arrow = true, -- 是否绘制方向指示箭头，仅在line_style为NONE时有效
                                has_start = true, -- 绘制起始点
                                has_end = true, -- 绘制终点
                                enable_alpha = true,
                                -- automatic_scale = true,
                                alignment = 'AlignCenter',
                                --enable_width = false,
                            },
                        },
                        styles = {
                            --["transit"] = MULTIPOINT_STYLE(), -- 这个是固定匹配经停点的属性
                            ["dyna_pass"] = LINE(),
                            ["dyna_remain"] = default
                        }
                    },
                }
            }
        },
    }
}
