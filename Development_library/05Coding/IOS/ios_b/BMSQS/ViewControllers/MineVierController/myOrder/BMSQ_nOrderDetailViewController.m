//
//  BMSQ_nOrderDetailViewController.m
//  BMSQS
//
//  Created by liuqin on 16/3/2.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_nOrderDetailViewController.h"
#import "SVProgressHUD.h"
#import "CouponImage.h"

@interface BMSQ_nOrderDetailViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView* m_tableView;

@property (nonatomic, strong)NSDictionary *resultDic;
@property (nonatomic, assign)BOOL isShow;


@end


@implementation BMSQ_nOrderDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavTitle:@"订单详情"];
    [self setNavBackItem];
    self.isShow = NO;
    [self setViewUp];
    [self getConsumeInfo];
}


- (void)setViewUp {
    

    
}

#pragma mark - 数据请求
- (void)getConsumeInfo
{
    [SVProgressHUD showWithStatus:@""];

    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    NSString* vcode = [gloabFunction getSign:@"getConsumeInfo" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getConsumeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.resultDic = responseObject;
        weakSelf.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
        weakSelf.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        weakSelf.m_tableView.dataSource = self;
        weakSelf.m_tableView.delegate = self;
        weakSelf.m_tableView.backgroundColor = [UIColor clearColor];
        [weakSelf.view addSubview:weakSelf.m_tableView];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}




-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 7;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
        return 1;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) { //时间
        return 20;
        
    }else if (indexPath.section ==2){ //条形码
            return 70;
    }
    else if (indexPath.section ==4){ //订单状态
        return 50;
    } else if (indexPath.section ==5){ //
        
        if (self.isShow)
            return 200;
        else
            return 40;
    }
    else{
        return 40;
    }
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        static NSString *identifi = @"n_couponDeatil0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 20)];
            nameLabel.font = [UIFont boldSystemFontOfSize:12.f];
            nameLabel.tag = 100;
            nameLabel.textAlignment = NSTextAlignmentRight;
            [cell addSubview:nameLabel];
            
        }
        
        UILabel *timeLabel = (UILabel *)[cell viewWithTag:100];
        timeLabel.text = [self.resultDic objectForKey:@"consumeTime"];
        return cell;
    }
    else if (indexPath.section ==1){
        static NSString *identifi = @"n_couponDeatil1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH-10, 39)];
            nameLabel.text = @"阿能面包";
            nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
            nameLabel.tag = 100;
            [cell addSubview:nameLabel];
        }
        UILabel *timeLabel = (UILabel *)[cell viewWithTag:100];
        timeLabel.text = [self.resultDic objectForKey:@"shopName"];
        
        return cell;
        
    }
    else if (indexPath.section ==2){
        static NSString *identifi = @"couponDeatil2";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 69)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UILabel *orderLable = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH-10, 20)];
            orderLable.font = [UIFont boldSystemFontOfSize:14.f];
            orderLable.tag = 100;
            [bgView addSubview:orderLable];
            
            UIImageView *QRImage =[[UIImageView alloc]initWithFrame:CGRectMake(0, 25, APP_VIEW_WIDTH, 40)];
            [bgView addSubview:QRImage];
            QRImage.tag = 200;

        }
        UILabel *orderLable = (UILabel *)[cell viewWithTag:100];
        orderLable.text = [NSString stringWithFormat:@"订单号:%@",[self.resultDic objectForKey:@"orderNbr"]];
        UIImageView *QRImage = (UIImageView *)[cell viewWithTag:200];
        [QRImage setImage:[CouponImage couponCodeQR:[self.resultDic objectForKey:@"orderNbr"]]];

        return cell;
    }
    else if (indexPath.section ==3){  //消费金额
        static NSString *identifi = @"couponDeatil3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 100, bgView.frame.size.height)];
            nameLabel.text = @"消费金额";
            nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
            [bgView addSubview:nameLabel];
            
            UILabel *Popularity = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-120, bgView.frame.size.height)];
            Popularity.font = [UIFont systemFontOfSize:14.f];
            Popularity.textColor = APP_TEXTCOLOR;
            Popularity.textAlignment = NSTextAlignmentRight;
            Popularity.tag = 100;
            [bgView addSubview:Popularity];
            
            
        }
        UILabel *nameLable = (UILabel *)[cell viewWithTag:100];
        float orderAmount = [[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"orderAmount"]]floatValue];
        nameLable.text = [NSString stringWithFormat:@"%.2f元",orderAmount];
        return cell;
    }
    else if (indexPath.section ==4){  //消费金额
        static NSString *identifi = @"n_couponDeatil4";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 39)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 100, bgView.frame.size.height)];
            nameLabel.text = @"订单状态";
            nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
            [bgView addSubview:nameLabel];
            
            UILabel *Popularity = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+20, 0, APP_VIEW_WIDTH-120, bgView.frame.size.height)];
            Popularity.font = [UIFont systemFontOfSize:14.f];
            Popularity.textColor = APP_TEXTCOLOR;
            Popularity.tag = 100;
            Popularity.text = @"已完成";
            [bgView addSubview:Popularity];
            
            
        }
        UILabel *orderStatusLabel = (UILabel *)[cell viewWithTag:100];
     
        orderStatusLabel.text = [CouponImage getOrderStatus:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"status"]]];
        return cell;
    }
    else if (indexPath.section ==5){  //消费金额
        static NSString *identifi = @"n_couponDeatil5";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 100, bgView.frame.size.height)];
            nameLabel.text = @"优惠金额";
            nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
            [bgView addSubview:nameLabel];
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-130, 0, 120, bgView.frame.size.height)];
            [button setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
            button.tag = 100;
            button.titleLabel.font = [UIFont systemFontOfSize:14.f];
            button.backgroundColor = [UIColor clearColor];
            [button setImage:[UIImage imageNamed:@"Arrow_down"] forState:UIControlStateNormal];
            [button addTarget:self action:@selector(clickBtn) forControlEvents:UIControlEventTouchUpInside];
            
            [button setImageEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -150)];
            
            [bgView addSubview:button];

            UIView *secView = [[UIView alloc]initWithFrame:CGRectMake(0, 40, APP_VIEW_WIDTH, 159)];
            secView.tag = 200;
            secView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:secView];
            
            
            UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 100, 30)];
            label1.text = @"工银优惠:";
            label1.font = [UIFont boldSystemFontOfSize:14.f];
            [secView addSubview:label1];
            UILabel *label2 = [[UILabel alloc]initWithFrame:CGRectMake(5, 30, 100, 30)];
            label2.text = @"会员卡:";
            label2.font = [UIFont boldSystemFontOfSize:14.f];
            [secView addSubview:label2];
            UILabel *label3 = [[UILabel alloc]initWithFrame:CGRectMake(5, 60, 100, 30)];
            label3.text = @"商家红包:";
            label3.font = [UIFont boldSystemFontOfSize:14.f];
            [secView addSubview:label3];
            UILabel *label4 = [[UILabel alloc]initWithFrame:CGRectMake(5, 90, 100, 30)];
            label4.text = @"惠圈红包:";
            label4.font = [UIFont boldSystemFontOfSize:14.f];
            [secView addSubview:label4];
            UILabel *label5 = [[UILabel alloc]initWithFrame:CGRectMake(5, 120, 100, 30)];
            label5.text = @"首单立减";
            label5.font = [UIFont boldSystemFontOfSize:14.f];
            [secView addSubview:label5];
            
            UILabel *label11 = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-120, 30)];
            label11.font = [UIFont systemFontOfSize:14.f];
            label11.textColor = APP_TEXTCOLOR;
            label11.textAlignment = NSTextAlignmentRight;
            label11.tag = 1000;
            label11.text = @"1000.00元";
            [secView addSubview:label11];
            UILabel *label22 = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 30, APP_VIEW_WIDTH-120, 30)];
            label22.font = [UIFont systemFontOfSize:14.f];
            label22.textColor = APP_TEXTCOLOR;
            label22.textAlignment = NSTextAlignmentRight;
            label22.tag = 2000;
            label22.text = @"1000.00元";
            [secView addSubview:label22];
            UILabel *label33 = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 60, APP_VIEW_WIDTH-120, 30)];
            label33.font = [UIFont systemFontOfSize:14.f];
            label33.textColor = APP_TEXTCOLOR;
            label33.textAlignment = NSTextAlignmentRight;
            label33.tag = 3000;
            label33.text = @"1000.00元";
            [secView addSubview:label33];
            UILabel *label44 = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 90, APP_VIEW_WIDTH-120,30)];
            label44.font = [UIFont systemFontOfSize:14.f];
            label44.textColor = APP_TEXTCOLOR;
            label44.textAlignment = NSTextAlignmentRight;
            label44.tag = 4000;
            label44.text = @"1000.00元";
            [secView addSubview:label44];
            UILabel *label55 = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 120, APP_VIEW_WIDTH-120, 30)];
            label55.font = [UIFont systemFontOfSize:14.f];
            label55.textColor = APP_TEXTCOLOR;
            label55.textAlignment = NSTextAlignmentRight;
            label55.tag = 5000;
            label55.text = @"1000.00元";
            [secView addSubview:label55];

            
            
            
        }
        UIButton *nameLable = (UIButton *)[cell viewWithTag:100];
        float orderAmount = [[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"deduction"]]floatValue];
        [nameLable setTitle:[NSString stringWithFormat:@"%.2f元",orderAmount] forState:UIControlStateNormal];
        UIView *secView = [cell viewWithTag:200];
        
        if (self.isShow) {
            secView.hidden = NO;
        }else{
            secView.hidden = YES;
        }

        UILabel *labe1 = (UILabel *)[cell viewWithTag:1000];
        UILabel *labe2 = (UILabel *)[cell viewWithTag:2000];
        UILabel *labe3 = (UILabel *)[cell viewWithTag:3000];
        UILabel *labe4 = (UILabel *)[cell viewWithTag:4000];
        UILabel *labe5 = (UILabel *)[cell viewWithTag:5000];
        labe1.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"bankCardDeduction"]];
        labe2.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"cardDeduction"]];
        labe3.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"shopBonusDeduction"]];
        labe4.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"platBonusDeduction"]];
        labe5.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"firstDeduction"]];


        
        return cell;
    }
    else{
        static NSString *identifi = @"couponDeatil3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, 100, bgView.frame.size.height)];
            nameLabel.text = @"实际支付";
            nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
            [bgView addSubview:nameLabel];
            
            UILabel *Popularity = [[UILabel alloc]initWithFrame:CGRectMake(nameLabel.frame.size.width+10, 0, APP_VIEW_WIDTH-120, bgView.frame.size.height)];
            Popularity.font = [UIFont systemFontOfSize:14.f];
            Popularity.textColor = APP_TEXTCOLOR;
            Popularity.textAlignment = NSTextAlignmentRight;
            Popularity.tag = 100;
            [bgView addSubview:Popularity];
            
            
        }
        UILabel *nameLable = (UILabel *)[cell viewWithTag:100];
        float orderAmount = [[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"realPay"]]floatValue];
        nameLable.text = [NSString stringWithFormat:@"%.2f元",orderAmount];
        return cell;
    }
}


-(void)clickBtn{
    self.isShow = !self.isShow;

    NSIndexSet *set = [[NSIndexSet alloc]initWithIndex:5];
     [self.m_tableView reloadSections:set withRowAnimation:UITableViewRowAnimationNone];
    
}


@end
