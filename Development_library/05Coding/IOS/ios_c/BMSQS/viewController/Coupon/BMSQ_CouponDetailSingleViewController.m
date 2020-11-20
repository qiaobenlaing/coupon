//
//  BMSQ_CouponDetailSingleViewController.m
//  BMSQC
//
//  Created by liuqin on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponDetailSingleViewController.h"
#import "BMSQ_ShopDetailController.h"
#import "BMSQ_CouponNbrCell.h"

#import "CouponImage.h"
#import "MobClick.h"
#import "BMSQ_PayDetailViewController.h"
#import "Pay_SecViewController.h"
#import "BMSQ_PayDetailSViewController.h"

@interface BMSQ_CouponDetailSingleViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView* m_tableView;
@property (nonatomic, strong)NSDictionary *resultDic;

@end


@implementation BMSQ_CouponDetailSingleViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CouponDetailSingle"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CouponDetailSingle"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    
    [self setNavTitle:@"优惠券详情"];
    [self setNavBackItem];
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:self.m_tableView];
    
    
    
    [self getUserCouponInfo];

}


#pragma mark - UITableView delegate

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 0;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==0 ) {
        return 0;
    }else{
        return 10;
    }
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 4;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section == 2)
        return 5;
    else
        return 1;
 
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        return 180;
        
    }else if (indexPath.section ==1){
        return 120;
    }
    else if (indexPath.section ==2){
        if (indexPath.row ==4) {
            NSString *remark = [self.resultDic objectForKey:@"remark"];
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30, MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                               context:nil].size;
            return size.height<20?50:50+size.height;
            
        }else{
            return 50;
        }
    }
    else if (indexPath.section ==3){
        return 80;
    }
    else{
        return 0;
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
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0,130, APP_VIEW_WIDTH, 50)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UIImageView *iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-70,90, 70, 70)];
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
            
        }
        UIImageView *iconImage = (UIImageView *)[cell viewWithTag:200];
        UILabel *nameLable = (UILabel *)[cell viewWithTag:300];
        UILabel *popuLabel = (UILabel *)[cell viewWithTag:400];
        NSString *imageStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.resultDic objectForKey:@"logoUrl"]];
        [iconImage sd_setImageWithURL:[NSURL URLWithString:imageStr] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
        nameLable.text = [self.resultDic objectForKey:@"shopName"];
        popuLabel.text = [NSString stringWithFormat:@"人气:%@ ",[self.resultDic objectForKey:@"popularity"]];
        
        
        UIScrollView *sc = (UIScrollView *)[cell viewWithTag:100];
        for (UIView *vview in sc.subviews) {
            [vview removeFromSuperview];
        }
        NSArray *imageArray = [self.resultDic objectForKey:@"shopDecoration"];
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
    else if (indexPath.section ==1){
        static NSString *identifi = @"couponDeatil1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            
            UIView *friView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
            friView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:friView];
            
            UILabel *messageLabe = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH-110, 40)];
            messageLabe.font = [UIFont systemFontOfSize:14.f];
            messageLabe.text = @"满0元打8折";
            messageLabe.tag = 100;
            [friView addSubview:messageLabe];
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-90, 5,70, 30)];
            button.layer.cornerRadius = 4;
            button.layer.masksToBounds = YES;
            [button setTitle:@"已过期" forState:UIControlStateNormal];
            [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            button.backgroundColor = APP_NAVCOLOR;
            button.titleLabel.font = [UIFont systemFontOfSize:13];
            [button addTarget:self action:@selector(useCoupon) forControlEvents:UIControlEventTouchUpInside];
            button.tag = 200;

            [friView addSubview:button];
            
            UIView *secView = [[UIView alloc]initWithFrame:CGRectMake(0, 41, APP_VIEW_WIDTH, 80)];
            secView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:secView];
            
            UILabel *couponCode = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH-110, 30)];
            couponCode.font = [UIFont systemFontOfSize:14.f];
            couponCode.text = @"优惠券编码:1231234124312";
            couponCode.textColor = APP_TEXTCOLOR;
            couponCode.tag = 300;
            [secView addSubview:couponCode];
            
            UIImageView *couponCodeQR = [[UIImageView alloc]initWithFrame:CGRectMake(30, 40, APP_VIEW_WIDTH-60, 40)];
            couponCodeQR.tag = 400;
            [secView addSubview:couponCodeQR];
        }
        
        UILabel *messageLabe = (UILabel *)[cell viewWithTag:100];
        UIButton *button = (UIButton *)[cell viewWithTag:200];
        UILabel *couponCode = (UILabel *)[cell viewWithTag:300];
        UIImageView *couponCodeQR =(UIImageView *)[cell viewWithTag:400];
        messageLabe.text = [self message:self.resultDic];
        NSString *userCouponNbr =[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"userCouponNbr"]];
        couponCode.text = [NSString stringWithFormat:@"优惠券编码:%@",userCouponNbr];
        [couponCodeQR setImage:[CouponImage couponCodeQR:userCouponNbr]];
        int i = [[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"status"]] intValue];
        [button setTitle:[self getSstatus:i] forState:UIControlStateNormal];
        
        if (i == 1|| i==5) {
            button.enabled = YES;
            button.backgroundColor = APP_NAVCOLOR;

        }else{
            button.enabled = NO;
            button.backgroundColor = APP_TEXTCOLOR;
  
        }


        return cell;
        
    }
    else if (indexPath.section ==2){
        
        
        static NSString *identifi = @"couponDeatil2";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.textLabel.numberOfLines = 0;
            cell.textLabel.font = [UIFont systemFontOfSize:13.f];
            cell.textLabel.textColor = APP_TEXTCOLOR;
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        if (indexPath.row ==0) {
            int typeCoupon = [[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"couponType"]]intValue];
            NSString *str = [NSString stringWithFormat:@"● 优惠券类型:\n   %@",[CouponImage couponTypeName:typeCoupon]];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }else if (indexPath.row ==1) {
            NSString *str = [NSString stringWithFormat:@"● 使用日期:\n   %@ ～ %@",[self.resultDic objectForKey:@"startUsingTime"],[self.resultDic objectForKey:@"expireTime"]];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }else if (indexPath.row==2){
            NSString *str = [NSString stringWithFormat:@"● 使用时间:\n   %@ ～ %@",[self.resultDic objectForKey:@"dayStartUsingTime"],[self.resultDic objectForKey:@"dayEndUsingTime"]];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        } else if (indexPath.row==3){
            NSString *status = [self getSstatus:[[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"status"]] intValue]];
            NSString *str = [NSString stringWithFormat:@"● 优惠券状态:\n   %@ ",status];
            NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
            [attStr addAttribute:NSForegroundColorAttributeName value:APP_NAVCOLOR range:NSMakeRange(0,1)];
            cell.textLabel.attributedText = attStr ;
        }else if(indexPath.row==4){
            NSString *remark = [self.resultDic objectForKey:@"remark"];
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
    else if (indexPath.section ==3){
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
        NSString *imageStr = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.resultDic objectForKey:@"logoUrl"]];
        [iconImage sd_setImageWithURL:[NSURL URLWithString:imageStr] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
        nameLable.text = [self.resultDic objectForKey:@"shopName"];
        popuLabel.text = [NSString stringWithFormat:@"人气:%@ ",[self.resultDic objectForKey:@"popularity"]];
        
        return cell;
    }
    
    else{
        static NSString *identifi = @"couponDeatil";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
        }
        return cell;
    }
    
    
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==3) {
        BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
        detailCtrl.shopCode = [self.resultDic objectForKey:@"shopCode"];
        detailCtrl.shopName = [self.resultDic objectForKey:@"shopName"];
        detailCtrl.shopImage = [self.resultDic objectForKey:@"logoUrl"];
        [self.navigationController pushViewController:detailCtrl animated:YES];
    }
}

#pragma mark - 数据请求
- (void)getUserCouponInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userD objectForKey:LONGITUDE];
    NSString *latitude = [userD objectForKey:LATITUDE];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    
    NSString* vcode = [gloabFunction getSign:@"getUserCouponInfo" strParams:self.userCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getUserCouponInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.resultDic = responseObject;
        [weakSelf.m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}
-(NSString *)getSstatus:(int )i{
//    1-可使用；2-已使用；4-已过期； 5-待使用
    NSString *resultStr;
    switch (i) {
        case 1:
        {
            resultStr = @"可使用";
        }
            break;
        case 2:
        {
            resultStr = @"已使用";
        }
            break;
        case 4:
        {
            resultStr = @"已过期";
        }
            break;
        case 5:
        {
            resultStr = @"待使用";
        }
            break;
            
        default:
        {
            resultStr = [NSString stringWithFormat:@"未知状态[%d]",i];
        }

            break;
    }
    return resultStr;
}

-(NSString *)message:(NSDictionary *)dicShare{
    
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
    return str;
}
#pragma mark 使用优惠券
-(void)useCoupon{
    
    
        int type =(int)[[self.resultDic objectForKey:@"couponType"]integerValue];
        
        switch (type) {
            case 1:  //N元购
            {
                
                [self payCard:self.resultDic];
            }
                break;
            case 2:
            {
            }
                break;
            case 3:   //抵扣券 折扣券 一样
            {
                [self buyClick:self.resultDic ];
                
            }
                break;
            case 4:   //折扣券 抵扣券 一样
            {
                [self buyClick:self.resultDic ];
            }
                break;
            case 5:    //实物券 体验券 一样
            {
                [self gotoPay_secVC:self.resultDic];
            }
                break;
            case 6:
            {
                [self gotoPay_secVC:self.resultDic ];
            }
                break;
            case 32:
            {
                [self buyClick:self.resultDic ];
            }
                break;
            case 33:
            {
                [self buyClick:self.resultDic ];
            }
                break;
            default:
                break;
        }
}

#pragma mark N元购
-(void)payCard:(NSDictionary *)dic{
    
    BMSQ_PayDetailViewController *vc = [[BMSQ_PayDetailViewController alloc]init];
    vc.shopCode =[dic objectForKey:@"shopCode"];
    vc.shopName = [dic objectForKey:@"shopName"];
    vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
    vc.formVc = (int)self.navigationController.viewControllers.count;
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark 实物 体验券使用
-(void)gotoPay_secVC:(NSDictionary *)dic{
    
    Pay_SecViewController *vc = [[Pay_SecViewController alloc]init];
    vc.myTitle =[dic objectForKey:@"shopName"] ;
    vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
    vc.shopCode = [dic objectForKey:@"shopCode"];
    vc.imageUrl =[dic objectForKey:@"logoUrl"];
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)buyClick:(NSDictionary *)dic{
    
    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
    vc.shopCode = [dic objectForKey:@"shopCode"];
    vc.shopName = [dic objectForKey:@"shopName"];
    vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    vc.type = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"couponType"]] intValue];
    vc.fromVC = (int)self.navigationController.viewControllers.count;
    vc.isNeedDiscount = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}
@end
