//
//  BMSQ_OrderViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_OrderViewController.h"
#import "BMSQ_CouponQRcodeControllerViewController.h"
#import "BMSQ_QR.h"
#import "BMSQ_PayCardViewController.h"

@interface BMSQ_OrderViewController ()<UITableViewDataSource,UITableViewDelegate>

@property(nonatomic,strong)NSMutableArray *leftArray;
@property(nonatomic,strong)NSMutableArray *rightArray;


@property(nonatomic, strong)UITableView *myTableView;

@end

@implementation BMSQ_OrderViewController

- (void)viewDidLoad {
    
    
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"支付详情"];
    
    self.view.backgroundColor =[ UIColor redColor];
    
    
    self.leftArray =[[NSMutableArray alloc]initWithArray:@[@"商家名称",@"消费",@"优惠券抵扣",@"会员卡折扣",@"红包抵扣",@"总抵扣",@"支付"]];
    self.rightArray = [[NSMutableArray alloc]init];
    
    [self initJsonPrcClient:@"2"];
    
    
    self.myTableView =[[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.myTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.myTableView.delegate = self;
    self.myTableView.dataSource = self;
    [self.view addSubview:self.myTableView];
    [self loadOrderMessage];
    
    
    
}


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return TABLEVIEWCELLH;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return  self.rightArray.count >0? self.rightArray.count +1:0;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row == self.rightArray.count) {
        
        static NSString *identifier = @"orderSubmitCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
          
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, [[UIScreen mainScreen]bounds].size.width-20, 35)];
            button.layer.cornerRadius = 2;
            button.layer.masksToBounds = YES;
            [button setTitle:@"在线支付" forState:UIControlStateNormal];
            button.backgroundColor = [UIColor colorWithRed:182/255.0 green:0 blue:12/255.0 alpha:1];
            [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            
            [button addTarget:self action:@selector(submit) forControlEvents:UIControlEventTouchUpInside];
            button.center = cell.center;
            [cell.contentView addSubview: button];
            
            
        }
        
        
        
        
        
        return cell;
        
    }else{
        
        
        static NSString *identifier = @"orderCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor whiteColor];
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 0, [[UIScreen mainScreen]bounds].size.width/2, TABLEVIEWCELLH)];
            leftLabel.textAlignment = NSTextAlignmentLeft;
            leftLabel.font = [UIFont systemFontOfSize:13.f];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.tag = 88;
            [cell.contentView addSubview:leftLabel];
            
            UILabel *riginLabel = [[UILabel alloc]initWithFrame:CGRectMake([[UIScreen mainScreen]bounds].size.width/2-10, 0, [[UIScreen mainScreen]bounds].size.width/2, TABLEVIEWCELLH)];
            riginLabel.textAlignment = NSTextAlignmentRight;
            riginLabel.font = [UIFont systemFontOfSize:14.f];
            riginLabel.textColor = [UIColor blackColor];
            riginLabel.tag = 99;
            [cell.contentView addSubview:riginLabel];
            
            UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, TABLEVIEWCELLH-1, [[UIScreen mainScreen]bounds].size.width, 0.5)];
            lineView.backgroundColor = APP_CELL_LINE_COLOR;
            [cell.contentView addSubview:lineView];
            
        }
        
        
        UILabel *left = (UILabel *)[cell viewWithTag:88];
        UILabel *rigin = (UILabel *)[cell viewWithTag:99];
        left.text = [self.leftArray objectAtIndex:indexPath.row];
        rigin.text = [self.rightArray objectAtIndex:indexPath.row];
        return  cell;

        
    }
    
    
    
}





#pragma mark --在线支付--
-(void)submit{
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.consumeAmount forKey:@"orderAmount"];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    [params setObject:self.platbouns forKey:@"platBonus"];
    [params setObject:self.shopbouns forKey:@"shopBonus"];
    
    NSString* vcode = [gloabFunction getSign:@"bankcardPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak __typeof(self)weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"bankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        
        NSString *code = [responseObject objectForKey:@"code"];
        if ([code intValue] ==50000) {
            
            
            BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
//            vc.shopName = weakSelf.shopName;
//            vc.price = [responseObject objectForKey:@"realPay"];
//            vc.orderNbr = [responseObject objectForKey:@"orderNbr"];   //订单号
//            vc.consumeCode = [responseObject objectForKey:@"consumeCode"];
//            vc.isOrder = YES;
            vc.fromVC = weakSelf.fromVC;
            [weakSelf.navigationController pushViewController:vc animated:YES];
            
        }else{
            int i =[code intValue];
            switch (i) {
                case 50314:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"商家编码错误",code]];
                    break;
                case 50317:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"请输入商家编码",code]];
                    break;
                case 50400:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"请输入消费金额",code]];
                    break;
                case 50401:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"消费金额不正确",code]];
                    break;
                case 50500:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"消请输入用户编码",code]];
                    break;
                case 50503:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"用户编码错误",code]];
                    break;
                case 50720:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"红包不可用",code]];
                    break;
                case 50724:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"红包已经过期",code]];
                    break;
                case 60501:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"订单不存在",code]];
                    break;
                case 80220:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"优惠券已经过期",code]];
                    break;
                case 80227:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"优惠券不可用",code]];
                    break;
                case 80400:
                    [self showAlertView:[NSString stringWithFormat:@"错误：%@[%@]",@"用户会员卡不可用",code]];
                    break;
                default:
                    [self showAlertView:[NSString stringWithFormat:@"错误：请联系相关人员[%@]",code]];

                    break;
            }
            
            
            
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        
        
        
    }];
    
    
    
    
    
}




-(void)loadOrderMessage{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.consumeAmount forKey:@"consumeAmount"];
    [params setObject:self.userCouponCode forKey:@"userCouponCode"];
    [params setObject:self.platbouns forKey:@"platBonus"];
    [params setObject:self.shopbouns forKey:@"shopBonus"];
    
    
    NSString* vcode = [gloabFunction getSign:@"getNewPrice" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getNewPrice" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
            [wself.rightArray addObject:self.shopName];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元",[self.consumeAmount floatValue]]]; //];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元", [[responseObject objectForKey:@"couponInsteadPrice"] floatValue]]];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元", [[responseObject objectForKey:@"cardInsteadPrice"]floatValue]]];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元",[self.platbouns floatValue]+[self.shopbouns floatValue]]];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元",[self.consumeAmount floatValue]-[[responseObject objectForKey:@"newPrice"] floatValue]]];
            [wself.rightArray addObject:[NSString stringWithFormat:@"%0.2f元",[[responseObject objectForKey:@"newPrice"] floatValue]]];
            [wself.myTableView  reloadData];
            wself.price = [responseObject objectForKey:@"newPrice"];
            
        }else{
            
            NSString *message = [NSString stringWithFormat:@"错误 : %@",[responseObject objectForKey:@"code"]];
            
            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:message delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
            [alterView show];
            
        }
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
    }];

    
}
@end
