//
//  BMSQ_CouponDetailViewController.m
//  BMSQC
//
//  Created by gh on 15/10/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponDetailViewController.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_CouponDetailCell.h"
#import "BMSQ_ShopDetailController.h"

#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"


#import "BMSQ_PayDetailViewController.h"
#import "BMSQ_PayDetailSViewController.h"
#import "Pay_SecViewController.h"

#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_CouponNbrCell.h"
#import "BMSQ_CouponOPViewController.h"
#import "BMSQ_CouponDetailViewController.h"
#import "MobClick.h"
@interface BMSQ_CouponDetailViewController ()<BMSQ_CouponNbrCellDelegate>

@property(nonatomic,strong)NSString *type;
@property(nonatomic,strong) NSString *status;

@property (nonatomic, strong)NSArray *couType;
@property (nonatomic, strong)UITableView* m_tableView;

@property (nonatomic, strong)NSDictionary *batchCouponInfo;
@property (nonatomic, strong)NSDictionary *shopInfo;
@property (nonatomic, strong)NSArray *recomShop;
@property (nonatomic, strong)NSArray *userCouponList;

@property (nonatomic, strong)NSMutableArray *couponSeleArray;

@end

@implementation BMSQ_CouponDetailViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CouponDetail"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CouponDetail"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    if (self.userCouponCode.length>0) {
        self.couponSeleArray = [[NSMutableArray alloc]init];
        [self setViewUp];
        [self getUserCouponInfo];

    }else{
        [self setNavTitle:@"优惠券详情"];
        [self setNavBackItem];
        
        UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        [imageView setImage:[UIImage imageNamed:@"iv_noMessage"]];
        [self.view addSubview:imageView];

    }
    
}



- (void)setViewUp {
    
    [self setNavTitle:@"优惠券详情"];
    [self setNavBackItem];
    

    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:self.m_tableView];
    
}

#pragma mark - UITableView delegate
-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]initWithFrame:CGRectZero];
    return view;
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    if (section ==4) {
        
        UIView *BgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 50)];
        BgView.backgroundColor = [UIColor clearColor];

        UILabel *Label = [[UILabel alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 35)];
        Label.backgroundColor = [UIColor whiteColor];
        Label.text = @"   推荐商家";
        Label.font = [UIFont systemFontOfSize:13.f];
        Label.textColor = APP_TEXTCOLOR;
        [BgView addSubview:Label];
        return BgView;
    }else if(section ==2){
        UIView *BgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 50)];
        BgView.backgroundColor = [UIColor clearColor];
        UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, 8, APP_VIEW_WIDTH, 34)];
        topView.backgroundColor = UICOLOR(249, 252, 171, 1) ;
        [BgView addSubview:topView];
 
        
        UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 130, 34)];
        label1.text = [NSString stringWithFormat:@"优惠券批次号%@",[self.batchCouponInfo objectForKey:@"batchNbr"]];
        label1.font = [UIFont systemFontOfSize:13.f];
        label1.textColor = APP_TEXTCOLOR;
        label1.backgroundColor =[UIColor clearColor];
        [topView addSubview:label1];
        
        
        
        NSString *couponType = [self.batchCouponInfo objectForKey:@"couponType"];
        NSString *str;
        switch ([couponType intValue]) {
            case 3:  //抵扣券
            {
                float availablePrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"availablePrice"]] floatValue];
                float insteadPrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"insteadPrice"]] floatValue];
                
                str= [NSString stringWithFormat:@"满%.2f元减%.2f元",availablePrice,insteadPrice];
                
            }
                break;
            case 4:  //折扣券
            {
                float availablePrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"availablePrice"]] floatValue];
                float discountPercent = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"discountPercent"]] floatValue];
                str= [NSString stringWithFormat:@"满%.2f元打%.2f折",availablePrice,discountPercent];
            }
                break;
            case 7:  //兑换券
            {
                float insteadPrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"insteadPrice"]] floatValue];

                str= [NSString stringWithFormat:@"1张%.2f元",insteadPrice];
            }
                break;
            case 8:  //代金券
            {
                float payPrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"payPrice"]] floatValue];
                float insteadPrice = [[NSString stringWithFormat:@"%@",[self.batchCouponInfo objectForKey:@"insteadPrice"]] floatValue];

                str= [NSString stringWithFormat:@"%.2f元代%.2f元",payPrice,insteadPrice];
                
            }
                break;
                
            default:
                break;
        }
        
        UILabel *label2 = [[UILabel alloc]initWithFrame:CGRectMake(140, 0, APP_VIEW_WIDTH-150, 34)];
        label2.text = str;
        label2.font = [UIFont systemFontOfSize:13.f];
        label2.textColor = UICOLOR(241, 111, 61, 1);
        label2.backgroundColor =[UIColor clearColor];
        label2.textAlignment = NSTextAlignmentRight;
        [topView addSubview:label2];
        
        return BgView;
    }
    
    else{
        UIView *view = [[UIView alloc]initWithFrame:CGRectZero];
        return view;
    }
   
}
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 0;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==2) {
        return 50;
    }
    else if (section ==4 ) {
        return 50;
    }else{
        return 0;
    }
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 5;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==0) {
        return 1;
    }else if (section ==2){
        return self.userCouponList.count;
    }
    else if (section ==3){
        if ( [self.batchCouponInfo objectForKey:@"function"]) {
            NSString *function = [self.batchCouponInfo objectForKey:@"function"];
            return function.length>0? 4: 3;
        }
        return 3;
    }
    else if (section ==4){
        return self.recomShop.count;
    }
    else{
        return 1;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        return 200;
    
    }else if (indexPath.section ==2){
        
        NSString *str = [self.couponSeleArray objectAtIndex:indexPath.row];
        if ([str isEqualToString:@"0"])
            return 70;
        else
            return 140;

    }
    else if (indexPath.section ==3){

     
//                    NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
//                    CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30, MAXFLOAT)
//                                                       options:NSStringDrawingUsesLineFragmentOrigin
//                                                    attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
//                                                       context:nil].size;
//                    return size.height<20?50:50+size.height;
        
        
        if (indexPath.row ==0) {//日期
            return 50;
        }else if (indexPath.row==1){//时间
            return 50;
        }else if (indexPath.row==2){
            if([self.batchCouponInfo objectForKey:@"function"]){
                
                NSString *function = [self.batchCouponInfo objectForKey:@"function"];
                if (function.length>0) {
                    return 50;

                }else{
                    NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
                    if (remark.length ==0) {
                        return 50;

                    }
                    CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30, MAXFLOAT)
                                                       options:NSStringDrawingUsesLineFragmentOrigin
                                                    attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                                       context:nil].size;
                    return size.height<20?50:50+size.height;
                }
            }else{
                NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
                if (remark.length ==0) {
                    return 50;
                }
                CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30, MAXFLOAT)
                                                   options:NSStringDrawingUsesLineFragmentOrigin
                                                attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                                   context:nil].size;
                return size.height<20?50:50+size.height;

            }
        } else{
            NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
            if (remark.length ==0) {
                return 50;
            }
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30, MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                               context:nil].size;
            return size.height<20?50:50+size.height;
        }

  
    }
    else if (indexPath.section ==4){
        return 80;
    }
    else{
        return 45;
    }
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        static NSString *identifi = @"couponDeatil0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            UIScrollView *sc = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 130)];
            sc.backgroundColor = [UIColor clearColor];
            sc.tag = 100;
            sc.pagingEnabled = YES;
            [cell addSubview:sc];
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 130, APP_VIEW_WIDTH, 70)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UIImageView *iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, 90, 70, 70)];
            [iconImage setImage:[UIImage imageNamed:@"iv_logNodata"]];
            iconImage.layer.cornerRadius = 4;
            iconImage.layer.masksToBounds = YES;
            iconImage.tag = 200;
            iconImage.backgroundColor = [UIColor whiteColor];
            [cell addSubview:iconImage];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH-80, 30)];
            nameLabel.text = @"阿能面包";
            nameLabel.font = [UIFont boldSystemFontOfSize:16.f];
            nameLabel.tag = 300;
            [bgView addSubview:nameLabel];
            
            UILabel *Popularity = [[UILabel alloc]initWithFrame:CGRectMake(5, 30, APP_VIEW_WIDTH-80, 20)];
            Popularity.text = @"人气：151";
            Popularity.font = [UIFont systemFontOfSize:11.f];
            Popularity.textColor = APP_TEXTCOLOR;
            Popularity.tag = 400;
            [bgView addSubview:Popularity];
            
            UILabel *time = [[UILabel alloc]initWithFrame:CGRectMake(5, 50, APP_VIEW_WIDTH-10, 20)];
            time.text = @"营业时间:09:00-21:00";
            time.font = [UIFont systemFontOfSize:11.f];
            time.textColor = APP_TEXTCOLOR;
            time.tag = 500;
            [bgView addSubview:time];

        }
        UIImageView *iconImage = (UIImageView *)[cell viewWithTag:200];
        UILabel *nameLable = (UILabel *)[cell viewWithTag:300];
        UILabel *popuLabel = (UILabel *)[cell viewWithTag:400];
        UILabel *timeLabel = (UILabel *)[cell viewWithTag:500];
        NSString *imageStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.shopInfo objectForKey:@"logoUrl"]];
        [iconImage sd_setImageWithURL:[NSURL URLWithString:imageStr] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
        nameLable.text = [self.shopInfo objectForKey:@"shopName"];
//        popuLabel.text = [NSString stringWithFormat:@"人气:%@   回头客:%@",[self.shopInfo objectForKey:@"popularity"],[self.shopInfo objectForKey:@"repeatCustomers"]];
         popuLabel.text = [NSString stringWithFormat:@"人气:%@ ",[self.shopInfo objectForKey:@"popularity"]];
        timeLabel.text = [NSString stringWithFormat:@"营业时间:%@",[self.shopInfo objectForKey:@"businessHoursString"]];
        UIScrollView *sc = (UIScrollView *)[cell viewWithTag:100];
        for (UIView *vview in sc.subviews) {
            [vview removeFromSuperview];
        }
        NSArray *imageArray = [self.shopInfo objectForKey:@"shopImg"];
        if (imageArray.count == 0) {
            UIImageView *imaegV = [[UIImageView alloc]initWithFrame:sc.bounds];
            [imaegV setImage:[UIImage imageNamed:@"iv_noDataHome"]];
            [sc addSubview:imaegV];
            
        }else{
            for(int i=0 ;i<imageArray.count;i++){
                NSDictionary *dic = [imageArray objectAtIndex:i];
                UIImageView *imaegV = [[UIImageView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, sc.frame.size.height)];
                NSString *imageStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"imgUrl"]];
                [imaegV sd_setImageWithURL:[NSURL URLWithString:imageStr] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
                [sc addSubview:imaegV];
            }
            sc.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, sc.frame.size.height);
        }
        return cell;
    }
    else if (indexPath.section ==2){
        static NSString *identifi = @"couponDeatil1";
        BMSQ_CouponNbrCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[BMSQ_CouponNbrCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.NbrDelegate = self;
            
        }
        
        cell.indexpath = (int)indexPath.row;
        NSString *str = [self.couponSeleArray objectAtIndex:indexPath.row];
        [cell creatNbrCell:[self.userCouponList objectAtIndex:indexPath.row] isShow:[str isEqualToString:@"1"]?YES:NO];
        return cell;
        
    }
    else if (indexPath.section ==3){
        static NSString *identifi = @"couponDeatil2";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.numberOfLines = 0;
            cell.textLabel.font = [UIFont systemFontOfSize:13.f];
            cell.textLabel.textColor = APP_TEXTCOLOR;
        }
        if (indexPath.row ==0) {
            NSString *str = [NSString stringWithFormat:@"● 使用日期:\n   %@ ～ %@",[self.batchCouponInfo objectForKey:@"startUsingTime"],[self.batchCouponInfo objectForKey:@"expireTime"]];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }else if (indexPath.row==1){
            NSString *str = [NSString stringWithFormat:@"● 使用时间:\n   %@ ～ %@",[self.batchCouponInfo objectForKey:@"dayStartUsingTime"],[self.batchCouponInfo objectForKey:@"dayEndUsingTime"]];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }else if (indexPath.row==2){
            if([self.batchCouponInfo objectForKey:@"function"]){
                
                NSString *function = [self.batchCouponInfo objectForKey:@"function"];
                if (function.length>0) {
                    NSString *str = [NSString stringWithFormat:@"● 使用功能:\n   %@",[self.batchCouponInfo objectForKey:@"function"]];
                    NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
                    [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
                    cell.textLabel.attributedText = attStr;
                }else{
                    NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
                    if (remark.length ==0) {
                        remark = @"暂无说明";
                    }
                    NSString *str =[NSString stringWithFormat:@"● 使用说明:\n   %@",remark];
                    NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
                    [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
                    cell.textLabel.attributedText = attStr ;
                }
            }else{
                NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
                if (remark.length ==0) {
                    remark = @"暂无说明";
                }
                NSString *str =[NSString stringWithFormat:@"● 使用说明:\n   %@",remark];
                NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
                [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
                cell.textLabel.attributedText = attStr ;
            }
        } else{
            NSString *remark = [self.batchCouponInfo objectForKey:@"remark"];
            if (remark.length ==0) {
                remark = @"暂无说明";
            }
            NSString *str =[NSString stringWithFormat:@"● 使用说明:\n   %@",remark];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }
        return cell;
    }
    else if (indexPath.section ==4){
        static NSString *identifi = @"couponDeatil3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
          
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 70)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UIImageView *iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 50, 50)];
            [iconImage setImage:[UIImage imageNamed:@"iv_logNodata"]];
            iconImage.layer.cornerRadius = 4;
            iconImage.layer.masksToBounds = YES;
            iconImage.tag = 200;
            iconImage.backgroundColor = [UIColor whiteColor];
            [cell addSubview:iconImage];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 10, APP_VIEW_WIDTH-90, 30)];
            nameLabel.text = @"阿能面包";
            nameLabel.font = [UIFont boldSystemFontOfSize:15.f];
            nameLabel.tag = 300;
            [bgView addSubview:nameLabel];
            
            UILabel *Popularity = [[UILabel alloc]initWithFrame:CGRectMake(80, 40, APP_VIEW_WIDTH-80, 20)];
            Popularity.text = @"人气：151";
            Popularity.font = [UIFont systemFontOfSize:11.f];
            Popularity.textColor = APP_TEXTCOLOR;
            Popularity.tag = 400;
            [bgView addSubview:Popularity];
            
            cell.accessoryType =UITableViewCellAccessoryDisclosureIndicator;
            
        }
        UIImageView *iconImage = (UIImageView *)[cell viewWithTag:200];
        UILabel *nameLable = (UILabel *)[cell viewWithTag:300];
        UILabel *popuLabel = (UILabel *)[cell viewWithTag:400];
        NSDictionary *dic = [self.recomShop objectAtIndex:indexPath.row];
        NSString *imageStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"logoUrl"]];
        [iconImage sd_setImageWithURL:[NSURL URLWithString:imageStr] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
        nameLable.text = [dic objectForKey:@"shopName"];
//        popuLabel.text = [NSString stringWithFormat:@"人气:%@   回头客:%@",[dic objectForKey:@"popularity"],[dic objectForKey:@"repeatCustomers"]];
        
         popuLabel.text = [NSString stringWithFormat:@"人气:%@",[dic objectForKey:@"popularity"]];
        return cell;
    }
    else{
          static NSString *identifi = @"couponDeatil";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.accessoryType =UITableViewCellAccessoryDisclosureIndicator;

            cell.textLabel.text = @"进入店铺";
            cell.textLabel.textColor = APP_TEXTCOLOR;
            cell.textLabel.font = [UIFont systemFontOfSize:14.f];
            
        }
        return cell;
    }
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==4) {
//        NSDictionary *dic = [self.recomShop objectAtIndex:indexPath.row];
//        BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
//        detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
//        detailCtrl.shopName = [dic objectForKey:@"shopName"];
//        detailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
//        [self.navigationController pushViewController:detailCtrl animated:YES];
        
        BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
        NSDictionary *dic = [self.recomShop objectAtIndex:indexPath.row];
        detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
        detailCtrl.userCode = [gloabFunction getUserCode];
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
    }else if (indexPath.section ==1){
        
//        BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
//        shopDetailCtrl.shopCode = [self.batchCouponInfo objectForKey:@"shopCode"];
////        shopDetailCtrl.shopName = [dic objectForKey:@"shopName"];
////        shopDetailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
////        shopDetailCtrl.hidesBottomBarWhenPushed = YES;
//        [self.navigationController pushViewController:shopDetailCtrl animated:YES];
        
        BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
        detailCtrl.shopCode = [self.batchCouponInfo objectForKey:@"shopCode"];
        detailCtrl.userCode = [gloabFunction getUserCode];
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
    }
}

#pragma mark - 数据请求
- (void)getUserCouponInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *couponCode = [NSString stringWithFormat:@"%@",self.userCouponCode];
    [params setObject:couponCode forKey:@"batchCouponCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];

    NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userD objectForKey:LONGITUDE];
    NSString *latitude = [userD objectForKey:LATITUDE];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];

    NSString* vcode = [gloabFunction getSign:@"getBatchCouponInfoHasUser" strParams:couponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getBatchCouponInfoHasUser" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        weakSelf.shopInfo = [responseObject objectForKey:@"shopInfo"];
        weakSelf.recomShop = [responseObject objectForKey:@"recomShop"];
        weakSelf.batchCouponInfo =[responseObject objectForKey:@"batchCouponInfo"];
        weakSelf.userCouponList = [responseObject objectForKey:@"userCouponList"];
        for (id abcd in weakSelf.userCouponList) {
            [weakSelf.couponSeleArray addObject:@"0"];
        }
        [weakSelf.m_tableView reloadData];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];
}

#pragma mark nbrCellDelegate
-(void)clickCouponType:(int)i{
    
    NSString *str = [self.couponSeleArray objectAtIndex:i];
    if ([str isEqualToString:@"0"]) {
        [self.couponSeleArray replaceObjectAtIndex:i withObject:@"1"];
    }else{
        [self.couponSeleArray replaceObjectAtIndex:i withObject:@"0"];
    }
    
    [self.m_tableView reloadData];
}

-(void)applicationCoupon:(NSDictionary *)dic{
    
   /* orderCouponStatus
    * 11-已退款
    * 12-申请退款
    * 20-退款
    * 30-不可退款
    */
    int i = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderCouponStatus"]] intValue];
    
    switch (i) {
        case 11:
        {
            
            BMSQ_CouponOPViewController *vc = [[BMSQ_CouponOPViewController alloc]init];
            NSString *str = [NSString stringWithFormat:@"%@Browser/orderCouponRefund?orderCode=%@",H5_URL,[dic objectForKey:@"orderCode"]];
            vc.urlStr = str;
            [self.navigationController pushViewController:vc animated:YES];

        }
            break;
        case 12:
        {

            
            BMSQ_CouponOPViewController *vc = [[BMSQ_CouponOPViewController alloc]init];
            NSString *str = [NSString stringWithFormat:@"%@Browser/orderCouponRefunding?orderCode=%@",H5_URL,[dic objectForKey:@"orderCode"]];
            vc.urlStr = str;
            [self.navigationController pushViewController:vc animated:YES];

            
        }
            break;
        case 20:
        {

            BMSQ_CouponOPViewController *vc = [[BMSQ_CouponOPViewController alloc]init];
            NSString *str = [NSString stringWithFormat:@"%@Browser/cApplyRefund?orderCode=%@",H5_URL,[dic objectForKey:@"orderCode"]];
            vc.urlStr = str;
            [self.navigationController pushViewController:vc animated:YES];

            
        }
            break;
//        case 30:
//        {
//            
//            NSString *str = [NSString stringWithFormat:@"%@Browser/orderCouponRefund?orderCode=%@",H5_URL,[dic objectForKey:@"orderCode"]];
//            vc.urlStr = str;
//            
//        }
//            break;
            
        default:
            break;
    }
    
    
    
    
}

@end
