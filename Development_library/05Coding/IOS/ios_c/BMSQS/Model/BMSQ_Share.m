//
//  BMSQ_Share.m
//  BMSQC
//
//  Created by liuqin on 15/12/30.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_Share.h"

@implementation BMSQ_Share


+(void)shareContent:(NSDictionary *)dicShare{
    NSNumber *couponType = [dicShare objectForKey:@"couponType"];
    
    NSString *str;
    
    if (couponType.intValue == 1)
    {
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    } else if (couponType.intValue == 3) {
        str = [NSString stringWithFormat:@"满%@元立减%@元",[dicShare objectForKey:@"availablePrice"],[dicShare objectForKey:@"insteadPrice"]];
        
    }else if (couponType.intValue == 4){
        
        str = [NSString stringWithFormat:@"满%@元打%0.1f折",[dicShare objectForKey:@"availablePrice"],[[dicShare objectForKey:@"discountPercent"] floatValue]];
        
    }else{
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    }
    
    NSString *city = [dicShare objectForKey:@"city"];
    NSString *title = [NSString stringWithFormat:@"【 %@ 】我分享你一张优惠券，手快有，手慢无",city];
    //    我分享你一张诺亚方舟电影院的优惠券，到惠圈，惠生活！
    NSString *shopName = [NSString stringWithFormat:@"%@", [dicShare objectForKey:@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@，我分享你一张%@的优惠券，到惠圈，惠生活！",[gloabFunction changeNullToBlank:str],shopName];
    
    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dicShare objectForKey:@"batchCouponCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    
    [self shareClick:remark imagePath:imagePath title:title url:url];
}

//分享优惠券
+(void)shareClick:(NSString *)remark imagePath:(NSString *)imagePath title:(NSString *)title url:(NSString *)url{

    
    id<ISSContent> publishContent = [ShareSDK content:remark
                                       defaultContent:nil
                                                image:[ShareSDK imageWithPath:imagePath]
                                                title:title
                                                  url:url
                                          description:remark
                                            mediaType:SSPublishContentMediaTypeNews];
    
    
    id<ISSAuthOptions> authOptions = [ShareSDK authOptionsWithAutoAuth:YES
                                                         allowCallback:YES
                                                         authViewStyle:SSAuthViewStyleFullScreenPopup
                                                          viewDelegate:nil
                                               authManagerViewDelegate:appDelegate.viewDelegate];
    
    //在授权页面中添加关注官方微博
    [authOptions setFollowAccounts:[NSDictionary dictionaryWithObjectsAndKeys:
                                    [ShareSDK userFieldWithType:SSUserFieldTypeName value:@"ShareSDK"],
                                    SHARE_TYPE_NUMBER(ShareTypeSinaWeibo),
                                    [ShareSDK userFieldWithType:SSUserFieldTypeName value:@"ShareSDK"],
                                    SHARE_TYPE_NUMBER(ShareTypeTencentWeibo),
                                    nil]];
    
    
    id<ISSShareActionSheetItem> QQSpaceItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeQQSpace]
                                                                                 icon:[ShareSDK getClientIconWithType:ShareTypeQQSpace]
                                                                         clickHandler:^{
                                                                             [ShareSDK shareContent:publishContent
                                                                                               type:ShareTypeQQSpace
                                                                                        authOptions:authOptions
                                                                                      statusBarTips:YES
                                                                                             result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                 
                                                                                                 if (state == SSPublishContentStateSuccess)
                                                                                                 {
                                                                                                     [self showAlertView:@"分享成功"];
                                                                                                     
                                                                                                 }
                                                                                                 else if (state == SSPublishContentStateFail)
                                                                                                 {
                                                                                                     [self showAlertView:@"分享失败"];
                                                                                                     
                                                                                                 }
                                                                                             }];
                                                                         }];
    
    id<ISSShareActionSheetItem> wxSessionItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeWeixiSession]
                                                                                   icon:[ShareSDK getClientIconWithType:ShareTypeWeixiSession]
                                                                           clickHandler:^{
                                                                               [ShareSDK shareContent:publishContent
                                                                                                 type:ShareTypeWeixiSession
                                                                                          authOptions:authOptions
                                                                                        statusBarTips:YES
                                                                                               result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                   
                                                                                                   if (state == SSPublishContentStateSuccess)
                                                                                                   {
                                                                                                       [self showAlertView:@"分享成功"];
                                                                                                       
                                                                                                   }
                                                                                                   else if (state == SSPublishContentStateFail)
                                                                                                   {
                                                                                                       [self showAlertView:@"分享失败"];
                                                                                                       
                                                                                                   }
                                                                                               }];
                                                                           }];
    
    id<ISSShareActionSheetItem> wxTimeLineItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeWeixiTimeline]
                                                                                    icon:[ShareSDK getClientIconWithType:ShareTypeWeixiTimeline]
                                                                            clickHandler:^{
                                                                                [ShareSDK shareContent:publishContent
                                                                                                  type:ShareTypeWeixiTimeline
                                                                                           authOptions:authOptions
                                                                                         statusBarTips:YES
                                                                                                result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                                    
                                                                                                    if (state == SSPublishContentStateSuccess)
                                                                                                    {
                                                                                                        [self showAlertView:@"分享成功"];
                                                                                                        
                                                                                                    }
                                                                                                    else if (state == SSPublishContentStateFail)
                                                                                                    {
                                                                                                        [self showAlertView:@"分享失败"];
                                                                                                        
                                                                                                    }
                                                                                                }];
                                                                            }];
    
    id<ISSShareActionSheetItem> QQItem = [ShareSDK shareActionSheetItemWithTitle:[ShareSDK getClientNameWithType:ShareTypeQQ]
                                                                            icon:[ShareSDK getClientIconWithType:ShareTypeQQ]
                                                                    clickHandler:^{
                                                                        [ShareSDK shareContent:publishContent
                                                                                          type:ShareTypeQQ
                                                                                   authOptions:authOptions
                                                                                 statusBarTips:YES
                                                                                        result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                                                                            
                                                                                            if (state == SSPublishContentStateSuccess)
                                                                                            {
                                                                                                [self showAlertView:@"分享成功"];
                                                                                                
                                                                                            }
                                                                                            else if (state == SSPublishContentStateFail)
                                                                                            {
                                                                                                [self showAlertView:@"分享失败"];
                                                                                                
                                                                                            }
                                                                                        }];
                                                                    }];
    
    
    
    NSArray *shareList = [ShareSDK customShareListWithType:QQSpaceItem,wxSessionItem,wxTimeLineItem,QQItem,nil];
    
    
    //创建容器
    id<ISSContainer> container = [ShareSDK container];
    
    
    
    //显示分享菜单
    [ShareSDK showShareActionSheet:container
                         shareList:shareList
                           content:publishContent
                     statusBarTips:YES
                       authOptions:authOptions
                      shareOptions:[ShareSDK defaultShareOptionsWithTitle:nil
                                                          oneKeyShareList:[NSArray defaultOneKeyShareList]
                                                           qqButtonHidden:NO
                                                    wxSessionButtonHidden:NO
                                                   wxTimelineButtonHidden:NO
                                                     showKeyboardOnAppear:NO
                                                        shareViewDelegate:appDelegate.viewDelegate
                                                      friendsViewDelegate:appDelegate.viewDelegate
                                                    picViewerViewDelegate:nil]
                            result:^(ShareType type, SSResponseState state, id<ISSPlatformShareInfo> statusInfo, id<ICMErrorInfo> error, BOOL end) {
                                
                                if (state == SSPublishContentStateSuccess)
                                {
                                    [self showAlertView:@"分享成功"];
                                    
                                }
                                else if (state == SSPublishContentStateFail)
                                {
                                    [self showAlertView:@"分享失败"];
                                    
                                }
                            }];
    
}

#pragma mark 弹窗消息
+ (void)showAlertView:(NSString *)msg
{
    UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:msg delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}
@end
