package com.sony.dtv.tvinput.provider;

import android.content.ContentValues;
import com.mediatek.twoworlds.tv.common.MtkTvChCommon;
import com.mediatek.twoworlds.tv.model.MtkTvChannelInfo;
import com.mediatek.twoworlds.tv.model.MtkTvDvbChannelInfo;

/* JADX INFO: loaded from: classes.dex */
public final class SonyChannelBuilder {
    private Long channel_content_id = 0L;
    private String display_number = null;
    private String display_name = null;
    private String logo_uri = null;
    private Integer original_network_id = null;
    private Integer transport_stream_id = null;
    private Integer service_id = null;
    private Integer type = 0;
    private Integer service_type = 1;
    private Integer prog_list_type = 1;
    private Integer browsable = 1;
    private Integer searchable = 1;
    private Integer version_number = null;
    private Integer physical_channel = null;
    private Integer favorite_channel_1 = 0;
    private Integer favorite_channel_2 = 0;
    private Integer favorite_channel_3 = 0;
    private Integer favorite_channel_4 = 0;
    private Integer favorite_order_1 = null;
    private Integer favorite_order_2 = null;
    private Integer favorite_order_3 = null;
    private Integer favorite_order_4 = null;
    private Integer pay_channel_flag = 0;
    private Integer sorting_channel = 1;
    private Long sorting_order = null;
    private Long mw_channel_id = 0L;
    private Integer major_channel_number = null;
    private Integer minor_channel_number = null;
    private Integer preset_number = null;
    private Integer program_lock = null;
    private Integer epg_sorting_order = null;
    private Integer grace_note_id = null;
    private String csx_channel_id = null;
    private String csx_channel_name = null;
    private String csx_channel_image_url = null;
    private Long csx_channel_update_time_utc_millis = null;
    private String csx_channel_number = null;
    private Integer broadcaster_id = null;
    private Integer broadcast_type = null;
    private Long channel_update_time_utc_millis = null;
    private byte[] internal_provider_data = null;

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("content_id", this.channel_content_id);
        contentValues.put(SonyTvContract.Channels.DISPLAY_NUMBER, this.display_number);
        contentValues.put(SonyTvContract.Channels.DISPLAY_NAME, this.display_name);
        contentValues.put(SonyTvContract.Channels.LOGO_URI, this.logo_uri);
        contentValues.put(SonyTvContract.Channels.ORIGINAL_NETWORK_ID, this.original_network_id);
        contentValues.put(SonyTvContract.Channels.TRANSPORT_STREAM_ID, this.transport_stream_id);
        contentValues.put(SonyTvContract.Channels.SERVICE_ID, this.service_id);
        contentValues.put(SonyTvContract.Channels.TYPE, this.type);
        contentValues.put(SonyTvContract.Channels.SERVICE_TYPE, this.service_type);
        contentValues.put("prog_list_type", this.prog_list_type);
        contentValues.put(SonyTvContract.Channels.BROWSABLE, this.browsable);
        contentValues.put(SonyTvContract.Channels.SEARCHABLE, this.searchable);
        contentValues.put(SonyTvContract.Channels.VERSION_NUMBER, this.version_number);
        contentValues.put(SonyTvContract.Channels.PHYSICAL_CHANNEL, this.physical_channel);
        contentValues.put(SonyTvContract.Channels.FAVORITE_CHANNEL_1, this.favorite_channel_1);
        contentValues.put(SonyTvContract.Channels.FAVORITE_CHANNEL_2, this.favorite_channel_2);
        contentValues.put(SonyTvContract.Channels.FAVORITE_CHANNEL_3, this.favorite_channel_3);
        contentValues.put(SonyTvContract.Channels.FAVORITE_CHANNEL_4, this.favorite_channel_4);
        contentValues.put(SonyTvContract.Channels.FAVORITE_ORDER_1, this.favorite_order_1);
        contentValues.put(SonyTvContract.Channels.FAVORITE_ORDER_2, this.favorite_order_2);
        contentValues.put(SonyTvContract.Channels.FAVORITE_ORDER_3, this.favorite_order_3);
        contentValues.put(SonyTvContract.Channels.FAVORITE_ORDER_4, this.favorite_order_4);
        contentValues.put(SonyTvContract.Channels.PAY_CHANNEL_FLAG, this.pay_channel_flag);
        contentValues.put(SonyTvContract.Channels.SORTING_CHANNEL, this.sorting_channel);
        contentValues.put(SonyTvContract.Channels.SORTING_ORDER, this.sorting_order);
        contentValues.put(SonyTvContract.Channels.MW_CHANNEL_ID, this.mw_channel_id);
        contentValues.put(SonyTvContract.Channels.MAJOR_CHANNEL_NUMBER, this.major_channel_number);
        contentValues.put(SonyTvContract.Channels.MINOR_CHANNEL_NUMBER, this.minor_channel_number);
        contentValues.put("preset_number", this.preset_number);
        contentValues.put(SonyTvContract.Channels.PROGRAM_LOCK, this.program_lock);
        contentValues.put(SonyTvContract.Channels.EPG_SORTING_ORDER, this.epg_sorting_order);
        contentValues.put(SonyTvContract.Channels.GRACE_NOTE_ID, this.grace_note_id);
        contentValues.put(SonyTvContract.Channels.CSX_CHANNEL_ID, this.csx_channel_id);
        contentValues.put(SonyTvContract.Channels.CSX_CHANNEL_NAME, this.csx_channel_name);
        contentValues.put(SonyTvContract.Channels.CSX_CHANNEL_IMAGE_URL, this.csx_channel_image_url);
        contentValues.put(SonyTvContract.Channels.CSX_CHANNEL_UPDATE_TIME_UTC_MILLIS, this.csx_channel_update_time_utc_millis);
        contentValues.put(SonyTvContract.Channels.CSX_CHANNEL_NUMBER, this.csx_channel_number);
        contentValues.put(SonyTvContract.Channels.BROADCASTER_ID, this.broadcaster_id);
        contentValues.put(SonyTvContract.Channels.BROADCAST_TYPE, this.broadcast_type);
        contentValues.put(SonyTvContract.Channels.CHANNEL_UPDATE_TIME_UTC_MILLIS, this.channel_update_time_utc_millis);
        Integer num = (Integer) null;
        contentValues.put("intval1", num);
        contentValues.put("intval2", num);
        contentValues.put("intval3", num);
        contentValues.put("intval4", (Integer) 0);
        contentValues.put("intval5", (Integer) 0);
        contentValues.put("intval6", (Integer) 0);
        String str = (String) null;
        contentValues.put("text1", str);
        contentValues.put("text2", str);
        contentValues.put("text3", str);
        contentValues.put("text4", str);
        contentValues.put(SonyTvContract.Channels.INTERNAL_PROVIDER_DATA, this.internal_provider_data);
        return contentValues;
    }

    public SonyChannelBuilder copyFromMtkInfo(MtkTvChannelInfo mtkTvChannelInfo) {
        this.display_number = "" + mtkTvChannelInfo.getChannelNumber();
        this.display_name = "" + mtkTvChannelInfo.getServiceName();
        this.broadcast_type = Integer.valueOf(mtkTvChannelInfo.getBroadcastType());
        copyFromMtkInfoProglistAndBroadcastType(mtkTvChannelInfo);
        copyFromMtkInfoServiceType(mtkTvChannelInfo);
        this.favorite_channel_1 = Integer.valueOf(convertBooleanToInt(Boolean.valueOf(mtkTvChannelInfo.isDigitalFavorites1Service())));
        this.favorite_channel_2 = Integer.valueOf(convertBooleanToInt(Boolean.valueOf(mtkTvChannelInfo.isDigitalFavorites2Service())));
        this.favorite_channel_3 = Integer.valueOf(convertBooleanToInt(Boolean.valueOf(mtkTvChannelInfo.isDigitalFavorites3Service())));
        this.favorite_channel_4 = Integer.valueOf(convertBooleanToInt(Boolean.valueOf(mtkTvChannelInfo.isDigitalFavorites4Service())));
        this.program_lock = Integer.valueOf(convertBooleanToInt(Boolean.valueOf((mtkTvChannelInfo.getNetworkMask() | MtkTvChCommon.SB_VNET_BLOCKED) != 0)));
        this.mw_channel_id = Long.valueOf(mtkTvChannelInfo.getChannelId());
        this.major_channel_number = Integer.valueOf(mtkTvChannelInfo.getChannelNumber());
        this.minor_channel_number = Integer.valueOf(mtkTvChannelInfo.getChannelMinorNumber());
        this.internal_provider_data = new com.sony.gtv.app.input.model.SonyChannel(mtkTvChannelInfo).getAsByteArray();
        return this;
    }

    private void copyFromMtkInfoProglistAndBroadcastType(MtkTvChannelInfo mtkTvChannelInfo) {
        if (this.broadcast_type.intValue() == 2) {
            copyFromMtkInfoTypeDvb(mtkTvChannelInfo);
            return;
        }
        if (this.broadcast_type.intValue() == 3) {
            copyFromMtkInfoTypeAtsc(mtkTvChannelInfo);
            return;
        }
        if (this.broadcast_type.intValue() == 5) {
            copyFromMtkInfoTypeIsdb(mtkTvChannelInfo);
            return;
        }
        if (this.broadcast_type.intValue() == 7) {
            copyFromMtkInfoTypeDtmb(mtkTvChannelInfo);
            return;
        }
        this.type = 0;
        if (this.broadcast_type.intValue() == 1) {
            this.prog_list_type = Integer.valueOf(SonyTvContract.Channels.PROG_LIST_TYPE_ANALOG);
        } else {
            this.prog_list_type = 0;
        }
    }

    private void copyFromMtkInfoTypeDvb(MtkTvChannelInfo mtkTvChannelInfo) {
        MtkTvDvbChannelInfo mtkTvDvbChannelInfo = (MtkTvDvbChannelInfo) mtkTvChannelInfo;
        this.original_network_id = Integer.valueOf(mtkTvDvbChannelInfo.getOnId());
        this.transport_stream_id = Integer.valueOf(mtkTvDvbChannelInfo.getTsId());
        this.service_id = Integer.valueOf(mtkTvDvbChannelInfo.getServiceId());
        if (mtkTvChannelInfo.getBroadcastMedium() == 1) {
            this.type = 131072;
            this.prog_list_type = 16908288;
            return;
        }
        if (mtkTvChannelInfo.getBroadcastMedium() == 3) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_DVB_S);
            if (mtkTvChannelInfo.isGeneralSatelliteService()) {
                this.prog_list_type = 16908544;
                return;
            } else {
                if (mtkTvChannelInfo.isPreferredSatelliteService()) {
                    this.prog_list_type = 16908545;
                    return;
                }
                return;
            }
        }
        if (mtkTvChannelInfo.getBroadcastMedium() == 2) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_DVB_C);
            if (mtkTvChannelInfo.isCamOperatorService()) {
                this.prog_list_type = 16908801;
            } else {
                this.prog_list_type = 16908288;
            }
        }
    }

    private void copyFromMtkInfoTypeAtsc(MtkTvChannelInfo mtkTvChannelInfo) {
        if (mtkTvChannelInfo.getBroadcastMedium() == 1) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_ATSC_T);
        } else if (mtkTvChannelInfo.getBroadcastMedium() == 2) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_ATSC_C);
        }
        this.prog_list_type = 16908288;
    }

    private void copyFromMtkInfoTypeIsdb(MtkTvChannelInfo mtkTvChannelInfo) {
        if (mtkTvChannelInfo.getBroadcastMedium() == 1) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_ISDB_TB);
            this.prog_list_type = 17039360;
        } else if (mtkTvChannelInfo.getBroadcastMedium() == 3) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_ISDB_S);
            this.prog_list_type = 17039872;
        } else if (mtkTvChannelInfo.getBroadcastMedium() == 2) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_ISDB_C);
            this.prog_list_type = 17040128;
        }
    }

    private void copyFromMtkInfoTypeDtmb(MtkTvChannelInfo mtkTvChannelInfo) {
        if (mtkTvChannelInfo.getBroadcastMedium() == 1) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_DTMB);
        } else if (mtkTvChannelInfo.getBroadcastMedium() == 3) {
            this.type = Integer.valueOf(SonyTvContract.Channels.TYPE_S_DMB);
        }
        this.prog_list_type = 16908288;
    }

    private void copyFromMtkInfoServiceType(MtkTvChannelInfo mtkTvChannelInfo) {
        int serviceType = mtkTvChannelInfo.getServiceType();
        if (1 == serviceType) {
            this.service_type = 1;
        } else if (2 == serviceType) {
            this.service_type = 2;
        } else {
            this.service_type = 0;
        }
    }

    private int convertBooleanToInt(Boolean bool) {
        return bool.booleanValue() ? 1 : 0;
    }

    public SonyChannelBuilder setChannelContentId(Long l) {
        this.channel_content_id = l;
        return this;
    }

    public SonyChannelBuilder setDisplayNumber(String str) {
        this.display_number = str;
        return this;
    }

    public SonyChannelBuilder setDisplayName(String str) {
        this.display_name = str;
        return this;
    }

    public SonyChannelBuilder setLogoUri(String str) {
        this.logo_uri = str;
        return this;
    }

    public SonyChannelBuilder setOriginalNetworkId(Integer num) {
        this.original_network_id = num;
        return this;
    }

    public SonyChannelBuilder setTransportStreamId(Integer num) {
        this.transport_stream_id = num;
        return this;
    }

    public SonyChannelBuilder setServiceId(Integer num) {
        this.service_id = num;
        return this;
    }

    public SonyChannelBuilder setType(Integer num) {
        this.type = num;
        return this;
    }

    public SonyChannelBuilder setServiceType(Integer num) {
        this.service_type = num;
        return this;
    }

    public SonyChannelBuilder setProgListType(Integer num) {
        this.prog_list_type = num;
        return this;
    }

    public SonyChannelBuilder setBrowsable(Integer num) {
        this.browsable = num;
        return this;
    }

    public SonyChannelBuilder setSearchable(Integer num) {
        this.searchable = num;
        return this;
    }

    public SonyChannelBuilder setVersionNumber(Integer num) {
        this.version_number = num;
        return this;
    }

    public SonyChannelBuilder setPhysicalChannel(Integer num) {
        this.physical_channel = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteChannel1(Integer num) {
        this.favorite_channel_1 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteChannel2(Integer num) {
        this.favorite_channel_2 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteChannel3(Integer num) {
        this.favorite_channel_3 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteChannel4(Integer num) {
        this.favorite_channel_4 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteOrder1(Integer num) {
        this.favorite_order_1 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteOrder2(Integer num) {
        this.favorite_order_2 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteOrder3(Integer num) {
        this.favorite_order_3 = num;
        return this;
    }

    public SonyChannelBuilder setFavoriteOrder4(Integer num) {
        this.favorite_order_4 = num;
        return this;
    }

    public SonyChannelBuilder setPayChannelFlag(Integer num) {
        this.pay_channel_flag = num;
        return this;
    }

    public SonyChannelBuilder setSortingChannel(Integer num) {
        this.sorting_channel = num;
        return this;
    }

    public SonyChannelBuilder setSortingOrder(Integer num) {
        this.sorting_order = Long.valueOf(num.longValue());
        return this;
    }

    public SonyChannelBuilder setSortingOrder(Long l) {
        this.sorting_order = l;
        return this;
    }

    public SonyChannelBuilder setMwChannelId(Long l) {
        this.mw_channel_id = l;
        return this;
    }

    public SonyChannelBuilder setMajorChannelNumber(Integer num) {
        this.major_channel_number = num;
        return this;
    }

    public SonyChannelBuilder setMinorChannelNumber(Integer num) {
        this.minor_channel_number = num;
        return this;
    }

    public SonyChannelBuilder setPresetNumber(Integer num) {
        this.preset_number = num;
        return this;
    }

    public SonyChannelBuilder setProgramLock(Integer num) {
        this.program_lock = num;
        return this;
    }

    public SonyChannelBuilder setEpgSortingOrder(Integer num) {
        this.epg_sorting_order = num;
        return this;
    }

    public SonyChannelBuilder setGraceNoteId(Integer num) {
        this.grace_note_id = num;
        return this;
    }

    public SonyChannelBuilder setCsxChannelId(String str) {
        this.csx_channel_id = str;
        return this;
    }

    public SonyChannelBuilder setCsxChannelName(String str) {
        this.csx_channel_name = str;
        return this;
    }

    public SonyChannelBuilder setCsxChannelImageUrl(String str) {
        this.csx_channel_image_url = str;
        return this;
    }

    public SonyChannelBuilder setCsxChannelUpdateTimeUtcMillis(Long l) {
        this.csx_channel_update_time_utc_millis = l;
        return this;
    }

    public SonyChannelBuilder setCsxChannelNumber(String str) {
        this.csx_channel_number = str;
        return this;
    }

    public SonyChannelBuilder setBroadcasterId(Integer num) {
        this.broadcaster_id = num;
        return this;
    }

    public SonyChannelBuilder setBroadcastType(Integer num) {
        this.broadcast_type = num;
        return this;
    }

    public SonyChannelBuilder setChannelUpdateTimeUtcMillis(Long l) {
        this.channel_update_time_utc_millis = l;
        return this;
    }

    public SonyChannelBuilder setInternalProviderData(byte[] bArr) {
        this.internal_provider_data = bArr;
        return this;
    }
}
