--Frame层
local frame_height_offset = 0 --层高偏移量
local frame_face_color = '0xfffffdf8' --路面颜色
local frame_face_alpha = false --路面透明
local frame_outLine_width = 1 --边框粗细
local frame_outLine_color = '0xffab893e' --边框颜色
local frame_outLine_alpha = false --是否透明 true | false
--Frame 3D
local frame_3d_face_on_bottom = true --3d面是否在下
local frame_3d_height = 0.5
local frame_3d_face_color = '0xffF9F9F1'
local frame_3d_face_alpha = false
local frame_3d_outLine_color = '0xffCCCCCC'
local frame_3d_outLine_width = 0.1
local frame_3d_outLine_height = 0.4
local frame_3d_left_side_color = '0xff898989'
local frame_3d_right_side_color = '0xff898989'
local frame_3d_top_side_color = '0xff97867d'
--Area层
local area_height_offset = 0 --层高偏移量
local area_face_color = '0xB6DFFF' --填充颜色
local area_face_alpha = false --面透明
local area_face_texture = null
local area_face_automatic_scale = null
local area_outLine_width = 0.2 --边框粗细
local area_outLine_color = '0x97D0FF' --边框颜色
local area_outLine_alpha = false --是否透明 true | false
--Area 3D
local area_3d_face_on_bottom = false --3d面是否在下
local area_3d_height = 1
local area_3d_face_color = '0xffB6DFFF'
local area_3d_face_alpha = false
local area_3d_outLine_color = '0xff97D0FF'
local area_3d_outLine_width = 0.2
local area_3d_outLine_height = 1
local area_3d_left_side_color = '0xff898989'
local area_3d_right_side_color = '0xff898989'
local area_3d_top_side_color = '0xff97867d'
local area_3d_texture = null
local area_3d_automatic_scale = null

--展示区
local exbt_solid = '0xffC0E0FC'
local exbt_stroke = '0xff4E7EAF'
local exbt_3Dheight = 2
--服务设施
local service_facility_solid = '0xff92C4FF'
local service_facility_stroke = '0xff4277AD'
local service_facility_3Dheight = 1.5
--商务设施
local business_facility_solid = '0xfff4e3c9'
local business_facility_stroke = '0xff827a6f'
local business_facility_3Dheight = 2
--公共设施
local public_facility_solid = '0xffCAEAB5'
local public_facility_stroke = '0xff6E8E59'
local public_facility_3Dheight = 1
--连通设施
local connected_facility_solid = '0xffA8EFEF'
local connected_facility_stroke = '0xff539694'
local connected_facility_3Dheight = 0.5
--辅助设施
local assist_facility_solid = '0xffEBEDF2'
local assist_facility_stroke = '0xff9FA5AA'
local assist_facility_3Dheight = 0.5
--中空区域
local null_solid = '0xffFFFFFF'
local null_stroke = '0xffFFFFFF'
local null_3Dheight = 0

--Area_text层
local area_txt_collision_detection = true --是否开启碰撞检测
local area_txt_aabbox_extend = 5 --碰撞检测大小
local area_txt_color = '0xFF585858'
local area_txt_field = 'display' --文字显示地图数据的哪个字段 英文是 englishName
local area_txt_size = 5 --文字大小
local area_txt_outline_color = '0xFFFFFFFF' --文字边框
local area_txt_outline_width = 1 --文字边框粗细
local area_txt_anchor_x = 0.5 --文字x锚点
local area_txt_anchor_y = 0.5 --文字y锚点

--navigate层
local navigateColor = '0xFF1fafe7' --导航线颜色 0xFFf68baf
local navigateWidth = 1.0 --导航线宽度
local navigate_height_offset = -0.2 --层高偏移量

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

-- Android
local function GET_CACHE_PATH()
    local engine = GetEngine()
    local properties = engine.properties

    return properties["cache_folder"]
end

-- 空类型样式，设置此类型的POI不渲染
local function NULLSTYLE()
    return {
        ['2d'] = {
            style = 'nullstyle',
        }
    }
end

local function DEFAULT_STYLE()
    return {
        ['2d'] = {
            style = 'polygon',
            face = {
                color = area_face_color,
                enable_alpha = area_face_alpha,
                texture = area_face_texture,
                automatic_scale = area_face_automatic_scale
            },
            outline = {
                color = area_outLine_color,
                width = area_outLine_width,
                --width = area_outLine_width,
                enable_alpha = area_outLine_alpha,
                enable_width = false,
            },
            left_side = {}
        },
        ['3d'] = {
            style = 'polygon',
            height_offset = 2.0;
            face_on_bottom = area_3d_face_on_bottom;
            height = area_3d_height;
            face = {
                color = area_3d_face_color,
                enable_alpha = area_3d_face_alpha,
                texture = area_3d_texture,
                automatic_scale = area_3d_automatic_scale
            },
            outline = {
                color = area_3d_outLine_color,
                width = area_3d_outLine_width,
                height = area_3d_outLine_height,
                enable_width = false,
                enable_alpha = false,
                left_side = {
                    color = area_3d_left_side_color
                },
                right_side = {
                    color = area_3d_right_side_color
                },
                top_side = {
                    color = area_3d_top_side_color
                }
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

local function Set3dColorWith(color, widthColor, topcolor, sidecolor, temph)

    if (temph == 0) then
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
                color = widthColor, --内部边框
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
                enable_alpha = false,
                enable_width = false,
                alignment = 'AlignRight',
            }
        }
    }
end

local function customfeaturelayer(imagename, a)
    return {
        height_offset = -0.2,
        renderer = {
            type = 'simple',
            ['2d'] = {
                style = 'icon',
                icon = imagename,
                --color = '0xFF698AE7',
                enable_alpha = false,
                width = 40,
                height = 40,
                -- has_start=true,
                --has_end=true,
                -- has_arrow=true,
                use_texture_origin_size = false,
                anchor_x = 0.5,
                anchor_y = 0.5
            },
        }
    }
end

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

--a:poi面颜色；b:poi边框颜色；c:poi边框宽；d:3d面是否在下；e:3d面高;f:面是否透视 g:outline颜色 h:顶面凹陷
local function COLOR_STYLE(a, b, c, d, e, f, g, h)
    style = DEFAULT_STYLE()
    -- 2d属性
    if a then style['2d'].face.color = a;
        style['3d'].outline.left_side.color = a;
        style['3d'].outline.right_side.color = a;
        style['3d'].outline.top_side.color = a
    end;

    if b then style['2d'].outline.color = b end;
    if c then style['2d'].outline.width = 5 end;
    if f then style['2d'].face.enable_alpha = f end;
    -- 3d属性
    if a then style['3d'].face.color = a end;
    if b then style['3d'].outline.color = b end;
    if c then style['3d'].outline.width = c end;
    --  if d then style['3d'].face_on_bottom = d end;
    -- style['3d'].face_on_bottom = true;
    --  style['3d'].outline.enable_edge_shadow = false;
    if e then style['3d'].height = e end
    if e then style['3d'].outline.height = e end
    if h then style['3d'].face_on_bottom = h else style['3d'].face_on_bottom = false end;
    return style
end

local function DEFAULT_ICON()
    return {
        ['2d'] = {
            style = 'icon',
            --icon = "icons/00000000.png",
            use_texture_origin_size = false,
            width = 30,
            height = 30,
            anchor_x = 0.5,
            anchor_y = 0.5
        }
    }
end

local function noAnchorTxtStyle()
    return {
        ['2d'] = {
            anchor_style = null,
            style = 'annotation',
            color = area_txt_color,
            --color = '0xffff0000',
            field = area_txt_field,
            size = area_txt_size,
            unit = 'pt',
            outline_color = area_txt_outline_color,
            outline_width = area_txt_outline_width,
            --outline_color = '0xffffffff',
            --outline_width = 1.0,
            anchor_x = area_txt_anchor_x,
            anchor_y = area_txt_anchor_y,
            aabbox_extend = area_txt_aabbox_extend,
        }
    }
end

local function ICON(a)
    style = DEFAULT_ICON()
    style['2d'].icon = a;
    return style
    --  return {
    --    ['2d'] = {
    --      style = 'icon',
    --      icon = a,
    --      use_texture_origin_size = false,
    --      width = 30,
    --      height = 30,
    --      anchor_x = 0.5,
    --      anchor_y = 0.5
    --    }
    --  }
end

local function loadStyleWithType(styleType)
    if styleType == 0 then --默认的英文
        area_txt_field = 'englishName'
    else
        area_txt_field = 'display'
    end

    if styleType == 1 then --1号配色

        exbt_solid = '0xffC0E0FC'; --0xffC0E0FC
        exbt_stroke = '0xff4E7EAF';

        service_facility_solid = '0xff92C4FF';
        service_facility_stroke = '0xff4277AD';

        business_facility_solid = '0xffF4E3C9';
        business_facility_stroke = '0xff827A6F';

        public_facility_solid = '0xffCAEAB5';
        public_facility_stroke = '0xff6E8E59';

        connected_facility_solid = '0xffA8EFEF';
        connected_facility_stroke = '0xff539694';

        assist_facility_solid = '0xffEBEDF2';
        assist_facility_stroke = '0xff9FA5AA';

        null_solid = '0xffFFFFFF';
        null_stroke = '0xffffffff';

        frame_face_color = '0xffFAFAFA'

        navigateColor = '0xffF68BAF'
    end

    if styleType == 2 then --2号配色

        exbt_solid = '0xffEEE3DF';
        exbt_stroke = '0xff7F7B79';

        service_facility_solid = '0xffE8E6E7';
        service_facility_stroke = '0xff7F7F7F';

        business_facility_solid = '0xffC0DAF2';
        business_facility_stroke = '0xff566168';

        public_facility_solid = '0xffCBE2E7';
        public_facility_stroke = '0xff717D7F';

        connected_facility_solid = '0xffE7F2DD';
        connected_facility_stroke = '0xff7C8277';

        assist_facility_solid = '0xffDDE0F2';
        assist_facility_stroke = '0xff74757C';

        null_solid = '0xffFFFFFF';
        null_stroke = '0xffFFFFFF';

        frame_face_color = '0xffFDFBEE'

        navigateColor = '0xffF68BAF'
    end

    if styleType == 3 then --3号配色

        exbt_solid = '0xff9AD6FA';
        exbt_stroke = '0xff374D59';

        service_facility_solid = '0xffE7EFFD';
        service_facility_stroke = '0xff515459';

        business_facility_solid = '0xffF8CDB6';
        business_facility_stroke = '0xff594A41';

        public_facility_solid = '0xffB5DED6';
        public_facility_stroke = '0xff3F4C4A';

        connected_facility_solid = '0xffF1DCDC';
        connected_facility_stroke = '0xff595151';

        assist_facility_solid = '0xffECF8E7';
        assist_facility_stroke = '0xff555953';

        null_solid = '0xffFFFFFF';
        null_stroke = '0xffFFFFFF';

        frame_face_color = '0xffFDFDFD'

        navigateColor = '0xff2BA0E6'
    end

    if styleType == 4 then --4号配色

        exbt_solid = '0xffADD6BA';
        exbt_stroke = '0xff3E4C43';

        service_facility_solid = '0xffE8EFCA';
        service_facility_stroke = '0xff57594C';

        business_facility_solid = '0xffF5D6AD';
        business_facility_stroke = '0xff594E3F';

        public_facility_solid = '0xffA9CBD9';
        public_facility_stroke = '0xff3C484C';

        connected_facility_solid = '0xffD5EFB4';
        connected_facility_stroke = '0xff505943';

        assist_facility_solid = '0xffB5D6D1';
        assist_facility_stroke = '0xff414C4B';

        null_solid = '0xffFFFFFF';
        null_stroke = '0xffFFFFFF';

        frame_face_color = '0xffFDFCF8'

        navigateColor = '0xff5A947A'
    end

    if styleType == 5 then --5号配色

        exbt_solid = '0xffFAAB9C';
        exbt_stroke = '0xff4C342F';

        service_facility_solid = '0xffE5CCDF';
        service_facility_stroke = '0xff4C444B';

        business_facility_solid = '0xffBBEFD4';
        business_facility_stroke = '0xff3C4C44';

        public_facility_solid = '0xffB8D2F5';
        public_facility_stroke = '0xff39424C';

        connected_facility_solid = '0xffF1D2B8';
        connected_facility_stroke = '0xff4C423A';

        assist_facility_solid = '0xffDBEEF5';
        assist_facility_stroke = '0xff444A4C';

        null_solid = '0xffFFFFFF';
        null_stroke = '0xffFFFFFF';

        frame_face_color = '0xffF8FDFF'

        navigateColor = '0xffD75C43'
    end
end

local function defaultStyle(styleType)
    loadStyleWithType(styleType);
    return {
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
                height_offset = area_height_offset,
                renderer = {
                    type = 'unique',
                    key = {
                        'id',
                        'category',
                    },
                    default =  SetPolygonStyle_3D('0xffE5E3FC', 3,'0xffB6B5C9'),
                    styles = {
                        [37000000] = SetPolygonStyle_3D('0xfffffdf8', 0.2,'0xffab893e'),
                        [23063000] = SetPolygonStyle_3D('0xffCDF1F9', 3,'0xffA4C1C7'),
                        [23043000] = SetPolygonStyle_3D('0xffCDF1F9', 0.2,'0xffA4C1C7'),
                        [23041000] = SetPolygonStyle_3D('0xffCDF1F9', 0.2,'0xffA4C1C7'),
                        [13093000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13075000] = SetPolygonStyle_3D('0xffF9DEDE', 3,'0xffC7B1B2'),
                        [13074000] = SetPolygonStyle_3D('0xffF9DEDE', 3,'0xffC7B1B2'),
                        [13071000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13061000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13031000] = SetPolygonStyle_3D('0xffF9DEDE', 3,'0xffC7B1B2'),
                        [13003000] = SetPolygonStyle_3D('0xffFDEDD1', 3,'0xffC9BDA6'),
                        [13002000] = SetPolygonStyle_3D('0xffFDEDD1', 3,'0xffC9BDA6'),
                        [11452000] = SetPolygonStyle_3D('0xffFFDCC0', 3,'0xffCCB09A'),
                        [11471000] = SetPolygonStyle_3D('0xffFFDCC0', 3,'0xffCCB09A'),
                        [13010000] = SetPolygonStyle_3D('0xffF9DEDE', 3,'0xffC7B1B2'),
                        [13011000] = SetPolygonStyle_3D('0xffFDEDD1', 3,'0xffC9BDA6'),
                        [13036000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13062000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13063000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [13141000] = SetPolygonStyle_3D('0xffF9DEDE', 3,'0xffC7B1B2'),
                        [15000000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [15009000] = SetPolygonStyle_3D('0xffFCF295', 3,'0xffC9C177'),
                        [24091000] = SetPolygonStyle_3D('0xffCDF1F9', 3,'0xffA4C1C7'),
                        [24093000] = SetPolygonStyle_3D('0xffCDF1F9', 0.2,'0xffA4C1C7'),
                        [24097000] = SetPolygonStyle_3D('0xffCDF1F9', 3,'0xffA4C1C7'),
                        -- [962248]=Set3dColorWith('0xffff0000','0xffff0000','0xffff0000','0xffff0000',3.0),
                        [23999000]=Set3dColorWith('0xffEaEaEa','0xffab893e','0xffab893e','0xffab893e',3.0),
                        [23062000]=Set3dColorWith('0xffffffff','0xffab893e','0xffab893e','0xffab893e',0.2),
                    }
                }
            },
            Area_text = {
                collision_detection = area_txt_collision_detection,
                font_path = GET_FONT_PATH(), -- 字体文件路径
                renderer = {
                    type = 'unique',
                    key = {
                        'id'
                    },
                    default = {
                        ['2d'] = {
                            anchor_style = {
                                style = 'icon',
                                unit = 'pt',
                                --                            icon_url = 'http://expo.palmap.cn/dataApi/boothInfo/logo/',
                                icon_url = 'http://api.ipalmap.com/logo/64/', -- 设置服务器图标下载地址
                                icon_cache = GET_CACHE_PATH(), -- 设置图标缓存地址
                                icon_online = 'logo', -- 使用哪个字段的名称作为图标资源名称
                                --                            icon_online = 'id',
                                use_texture_origin_size = false, -- 是否按照原始尺寸显示图标，如果为true，则下面宽高无效
                                width = 6,
                                height = 6,
                            },
                            style = 'annotation',
                            color = area_txt_color,
                            --color = '0xffff0000',
                            field = area_txt_field,
                            size = area_txt_size,
                            unit = 'pt',
                            outline_color = area_txt_outline_color,
                            outline_width = area_txt_outline_width,
                            --outline_color = '0xffffffff',
                            --outline_width = 1.0,
                            anchor_x = area_txt_anchor_x,
                            anchor_y = area_txt_anchor_y,
                            aabbox_extend = area_txt_aabbox_extend,
                        }
                    },
                    styles = {
                        [1439750] = NULLSTYLE(), --去掉峰会地图中 展厅一 的文字显示效果.

                        [1888085] = noAnchorTxtStyle(), --去掉振华外面路的文字前图标
                        [1888086] = noAnchorTxtStyle(),
                        [1888087] = noAnchorTxtStyle(),

                        [1888091] = noAnchorTxtStyle(),
                        [1888092] = noAnchorTxtStyle(),
                        [1888093] = noAnchorTxtStyle()
                    }
                },
            },
            Facility = {
                height_offset = -0.2;
                collision_detection = true,
                renderer = {
                    type = 'unique',
                    key = {
                        'category'
                    },
                    default = {
                        ['2d'] = {
                            style = 'icon',
                            -- icon = "icons/1.png", -- 只要配置了当前属性，就加载本地图片
                            -- icon = "icons/compress/9_4bb.pvr",
                            icon_url = 'http://api.ipalmap.com/logo/64/', -- 设置服务器图标下载地址
                            icon_cache = GET_CACHE_PATH(), -- 设置图标缓存地址
                            icon_online = 'logo',
                            anchor_x = 0.5,
                            anchor_y = 0.5,
                            unit = 'pt',
                            use_texture_origin_size = false,
                            width = 5,
                            height = 5,
                        },
                    },
                    styles = {}
                }
            },
            navigate = { -- 导航图层参考样式设置
                -- height_offset = -10.2,
                height_offset = -0.2,
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
    };
end

CONFIG = {
    views = {
        style999 = defaultStyle(); --默认配色

        default0 = defaultStyle(); --默认配色
        default1 = defaultStyle(); --默认配色
        default2 = defaultStyle(); --默认配色
        default3 = defaultStyle(); --默认配色
        default4 = defaultStyle(); --默认配色
        default5 = defaultStyle(); --默认配色
        default6 = defaultStyle(); --默认配色

        style998 = defaultStyle(0); --默认的英文配色
        style1 = defaultStyle(1);
        style2 = defaultStyle(2);
        style3 = defaultStyle(3);
        style4 = defaultStyle(4);
        style5 = defaultStyle(5);
    }
}
