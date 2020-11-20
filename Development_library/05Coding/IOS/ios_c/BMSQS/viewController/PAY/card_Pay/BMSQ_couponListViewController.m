//
//  BMSQ_couponListViewController.m
//  BMSQC
//
//  Created by liuqin on 15/11/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_couponListViewController.h"
#import "SVProgressHUD.h"


@interface BMSQ_couponListViewController ()<UITableViewDataSource,UITableViewDelegate>

@property(nonatomic,strong)UITableView *my_tableView;

@property(nonatomic,strong)NSMutableArray *couponList;

@property(nonatomic,assign)int nbr;
@property(nonatomic,assign)float disMoney;


@property(nonatomic, strong)NSMutableArray *seletype3;


@property (nonatomic, strong)NSDictionary *seleDic;

@property (nonatomic,assign)BOOL isHave;  //YES 已领取 NO 没有领取

@end

@implementation BMSQ_couponListViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"订单结算"];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(listUserNoGrabCouponWhenPay) name:@"listUserNoGrabCouponWhenPay" object:nil];   //没有数据请求第二个接口
    
    
    self.couponList = [[NSMutableArray alloc]init];
    self.seletype3 = [[NSMutableArray alloc]init];
    self.nbr = 0;

    self.my_tableView =[[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-45)];
    self.my_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.my_tableView.delegate = self;
    self.my_tableView.dataSource = self;
    self.my_tableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.my_tableView];
    
    
    UIButton *submitButton = [[UIButton alloc]initWithFrame:CGRectMake(10,APP_VIEW_HEIGHT-35, APP_VIEW_WIDTH-20, 30)];
    submitButton.layer.masksToBounds = YES;
    submitButton.layer.cornerRadius = 5;
    [submitButton setTitle:@"去结算" forState:UIControlStateNormal];
    [submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    submitButton.titleLabel.font = [UIFont boldSystemFontOfSize:13.f];
    submitButton.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [self.view addSubview:submitButton];
    
    [submitButton addTarget:self action:@selector(clickSubmit) forControlEvents:UIControlEventTouchUpInside];
    
    [self listUserCouponWhenPay];

}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"couponListCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(12, 10, APP_VIEW_WIDTH-24, 80)];
        bgView.backgroundColor = [UIColor whiteColor];
        [cell addSubview:bgView];
        
        
//        radio_yes
        UIButton *seleButton = [[UIButton alloc]initWithFrame:CGRectMake(10, bgView.frame.size.height-30, 20, 20)];
        [seleButton setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateNormal];
        seleButton.tag = 111;
        [bgView addSubview:seleButton];
        seleButton.hidden = YES;
        
        
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(bgView.frame.size.width/4*3, 5, 1, bgView.frame.size.height-10)];
        lineView.backgroundColor = self.view.backgroundColor;
        [bgView addSubview:lineView];
        
        UIView *countView = [[UILabel alloc]initWithFrame:CGRectMake(lineView.frame.origin.x, 0, bgView.frame.size.width-lineView.frame.origin.x, bgView.frame.size.height)];
        countView.tag = 299;
        [bgView addSubview:countView];
        
        UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(lineView.frame.origin.x, 0, bgView.frame.size.width-lineView.frame.origin.x, bgView.frame.size.height-25)];
        countLabel.text = @"X12";
        countLabel.tag = 99;
        countLabel.font = [UIFont systemFontOfSize:18];
        countLabel.textAlignment = NSTextAlignmentCenter;
        countLabel.backgroundColor = [UIColor clearColor];
        countLabel.textColor = [UIColor whiteColor];
        [bgView addSubview:countLabel];
        
        UIView *type3View = [[UIView alloc]initWithFrame:CGRectMake(lineView.frame.origin.x, 0, bgView.frame.size.width-lineView.frame.origin.x, bgView.frame.size.height)];
        type3View.backgroundColor = UICOLOR(235, 136, 22, 1);
        type3View.tag = 1000;
        [bgView addSubview:type3View];
        type3View.hidden = YES;
        
        UILabel *countLa = [[UILabel alloc]initWithFrame:CGRectMake(type3View.frame.size.width/2-10, type3View.frame.size.height/2-8, 20, 16)];
        countLa.backgroundColor = [UIColor clearColor];
        countLa.font = [UIFont systemFontOfSize:12];
        countLa.text = @"10";
        countLa.tag = 999;
        countLa.textColor = [UIColor whiteColor];
        countLa.textAlignment = NSTextAlignmentCenter;
        [type3View addSubview:countLa];
        
        
        UIButton *minusButton = [[UIButton alloc]initWithFrame:CGRectMake(countLa.frame.origin.x-20, type3View.frame.size.height/2-10, 20, 20)];
        [minusButton setImage:[UIImage imageNamed:@"order_minus"] forState:UIControlStateNormal];
        [minusButton addTarget:self action:@selector(addNbr:) forControlEvents:UIControlEventTouchUpInside];
        minusButton.tag = 77;
        [type3View addSubview:minusButton];
        
        UIButton *addButton = [[UIButton alloc]initWithFrame:CGRectMake(countLa.frame.origin.x+countLa.frame.size.width, minusButton.frame.origin.y, 20, 20)];
        [addButton setImage:[UIImage imageNamed:@"order_add"] forState:UIControlStateNormal];
        [addButton addTarget:self action:@selector(addNbr:) forControlEvents:UIControlEventTouchUpInside];
        addButton.tag = 88;
        [type3View addSubview:addButton];

        
        UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, addButton.frame.origin.y+addButton.frame.size.height+5, type3View.frame.size.width, 20)];
        desLabel.backgroundColor = [UIColor clearColor];
        desLabel.font = [UIFont systemFontOfSize:11];
        desLabel.textAlignment = NSTextAlignmentCenter;
        desLabel.text = @"减免10元";
        desLabel.tag = 888;
        desLabel.textColor = [UIColor whiteColor];
        [type3View addSubview:desLabel];
        type3View.hidden = YES;
        
        
        UILabel *discountLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, bgView.frame.size.width-countLabel.frame.size.width-100, 30)];
        discountLabel.textAlignment = NSTextAlignmentLeft;
        discountLabel.font = [UIFont systemFontOfSize:12.f];
        discountLabel.backgroundColor = [UIColor clearColor];
        discountLabel.tag = 100;
        discountLabel.textColor = APP_TEXTCOLOR;
        [bgView addSubview:discountLabel];
        
        UILabel *functionLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 30, bgView.frame.size.width-countLabel.frame.size.width-50, 10)];
        functionLabel.textAlignment = NSTextAlignmentLeft;
        functionLabel.font = [UIFont systemFontOfSize:13.f];
        functionLabel.backgroundColor = [UIColor clearColor];
        functionLabel.tag = 101;
        functionLabel.text = @"仅限于剁椒鱼头仅限于剁椒鱼头";
        functionLabel.textColor = [UIColor colorWithRed:83/255.0 green:83/255.0 blue:83/255.0 alpha:1];
        [bgView addSubview:functionLabel];
        
        
        UILabel *piciLabel = [[UILabel alloc]initWithFrame:CGRectMake(discountLabel.frame.origin.x+discountLabel.frame.size.width, 0,95 , 30)];
        piciLabel.text = @"批次1234567";
        piciLabel.textAlignment = NSTextAlignmentCenter;
        piciLabel.font = [UIFont systemFontOfSize:12.f];
        piciLabel.backgroundColor = [UIColor clearColor];
        piciLabel.tag = 10001;
        piciLabel.textColor = APP_TEXTCOLOR;
        [bgView addSubview:piciLabel];
    
        
        UILabel *DateLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 40, bgView.frame.size.width-countLabel.frame.size.width-10, 20)];
        DateLabel.text = @"2015.11.11-2015.12.12";
        DateLabel.textAlignment = NSTextAlignmentLeft;
        DateLabel.font = [UIFont systemFontOfSize:11.f];
        DateLabel.tag = 200;
        DateLabel.backgroundColor = [UIColor clearColor];
        DateLabel.textColor = APP_TEXTCOLOR;
        [bgView addSubview:DateLabel];
        
        UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 60, bgView.frame.size.width-countLabel.frame.size.width-10, 20)];
        timeLabel.text = @"18:00:00-21:00:00";
        timeLabel.textAlignment = NSTextAlignmentLeft;
        timeLabel.font = [UIFont systemFontOfSize:11.f];
        timeLabel.backgroundColor = [UIColor clearColor];
        timeLabel.tag = 300;
        timeLabel.textColor = APP_TEXTCOLOR;
        [bgView addSubview:timeLabel];
        
        UILabel *isSend = [[UILabel alloc]initWithFrame:CGRectMake(bgView.frame.size.width-countLabel.frame.size.width-30, 55,20, 20)];
        isSend.layer.masksToBounds = YES;
        isSend.layer.cornerRadius = 3;
        isSend.text = @"送";
        isSend.font = [UIFont boldSystemFontOfSize:13.f];
        isSend.backgroundColor = [UIColor colorWithRed:215/255.0 green:15/255.0 blue:31/255.0 alpha:1];
        isSend.tag = 400;
        isSend.textAlignment = NSTextAlignmentCenter;
        isSend.textColor = [UIColor whiteColor];
        [bgView addSubview:isSend];
        isSend.hidden = YES;
        
        UILabel *typeLabel = [[UILabel alloc]initWithFrame:CGRectMake(countLabel.frame.origin.x, 55, countLabel.frame.size.width, 25)];
        typeLabel.tag = 199;
        typeLabel.font = [UIFont boldSystemFontOfSize:13.f];
        typeLabel.textAlignment = NSTextAlignmentCenter;
        typeLabel.textColor = [UIColor whiteColor];
        typeLabel.backgroundColor = [UIColor redColor];
        [bgView addSubview:typeLabel];
    }
    
    
    UILabel *countLabel = (UILabel *)[cell viewWithTag:99];
    UILabel *typeLabel = (UILabel *)[cell viewWithTag:199];
    UIView *bgColorView = (UILabel *)[cell viewWithTag:299];;
    
    if([self.couponType isEqualToString:@"3"]){
        bgColorView.backgroundColor = UICOLOR(235, 136, 22, 1);
        typeLabel.backgroundColor = UICOLOR(219, 71, 10, 1);
        typeLabel.text = @"抵扣券";
    }else{
        bgColorView.backgroundColor =  UICOLOR(235, 77, 83, 1);
        typeLabel.backgroundColor = UICOLOR(218, 15, 31, 1);
        typeLabel.text = @"折扣券";
    }
    

    UILabel *disLabel = (UILabel *)[cell viewWithTag:100];
    UILabel *fountionLabel = (UILabel *)[cell viewWithTag:101];

    UILabel *dateLabel = (UILabel *)[cell viewWithTag:200];
    UILabel *timeLabel = (UILabel *)[cell viewWithTag:300];
    UILabel *isSend = (UILabel *)[cell viewWithTag:400];

    
    UIView *type3View = (UIView *)[cell viewWithTag:1000];
    UILabel *nbrLabel = (UILabel *)[cell viewWithTag:999];
    UILabel *desLabel = (UILabel *)[cell viewWithTag:888];
    UIButton *seleButton = (UIButton *)[cell viewWithTag:111];
    UILabel *piciLabel = (UILabel *)[cell viewWithTag:10001];
    NSString *key = [self.seletype3 objectAtIndex:indexPath.row];
    if([key isEqualToString:@"1"]){
        seleButton.hidden = NO;
    }else{
        seleButton.hidden = YES;

    }
    NSDictionary *couponDic = [self.couponList objectAtIndex:indexPath.row];

    fountionLabel.text = [couponDic objectForKey:@"fountion"];
    if ([[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"isSend"]]intValue] ==1) {
        isSend.hidden = NO;
    }else{
        isSend.hidden = YES;

    }
    
    piciLabel.text = [NSString stringWithFormat:@"批次号:%@",[couponDic objectForKey:@"batchNbr"]];
    
    if ([self.couponType isEqualToString:@"3"]) {

        NSMutableAttributedString *mutableStr = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"￥%@ 满%@元可用",[couponDic objectForKey:@"insteadPrice"],[couponDic objectForKey:@"availablePrice"]]];
        
        NSDictionary *attributeDict = [NSDictionary dictionaryWithObjectsAndKeys:
                                       [UIFont systemFontOfSize:17.0],NSFontAttributeName,
                                       [UIColor colorWithRed:189/255.0 green:61/255.0 blue:9/255.0 alpha:1] ,NSForegroundColorAttributeName,[UIFont fontNamesForFamilyName:@"Arial"],NSFontAttributeName,
                                       nil];
        NSString *str1 = [NSString stringWithFormat:@"￥%@",[couponDic objectForKey:@"insteadPrice"]];
        [mutableStr addAttributes:attributeDict range:NSMakeRange(0,str1.length)];
        
        NSDictionary *attributeDict2 = [NSDictionary dictionaryWithObjectsAndKeys:
                                       [UIFont systemFontOfSize:11.0],NSFontAttributeName,
                                       [UIColor colorWithRed:218/255.0 green:97/255.0 blue:34/255.0 alpha:1] ,NSForegroundColorAttributeName,[UIFont fontNamesForFamilyName:@"Arial"],NSFontAttributeName,
                                       nil];
        NSString *str2 = [NSString stringWithFormat:@"满%@元可用",[couponDic objectForKey:@"availablePrice"]];
        [mutableStr addAttributes:attributeDict2 range:NSMakeRange(str1.length+1,str2.length)];
        disLabel.attributedText = mutableStr;
        NSString *str = [self.seletype3 objectAtIndex:indexPath.row];
        if ([str isEqualToString:@"1"]) {
            countLabel.hidden = YES;
            type3View.hidden = NO;
            nbrLabel.text = [NSString stringWithFormat:@"%d",self.nbr];
            desLabel.text = [NSString stringWithFormat:@"减免%0.2f元",self.disMoney];
        }else{
            countLabel.hidden = NO;
            type3View.hidden = YES;
        }
        
    }else{
        float discountPercent =[[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"discountPercent"]] floatValue];
        
        NSMutableAttributedString *mutableStr = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"打%0.1f折 满%@元可用",discountPercent,[couponDic objectForKey:@"availablePrice"]]];
        
        NSDictionary *attributeDict = [NSDictionary dictionaryWithObjectsAndKeys:
                                       [UIFont systemFontOfSize:17.0],NSFontAttributeName,
                                       [UIColor colorWithRed:189/255.0 green:61/255.0 blue:9/255.0 alpha:1] ,NSForegroundColorAttributeName,[UIFont fontNamesForFamilyName:@"Arial"],NSFontAttributeName,
                                       nil];
        NSString *str1 = [NSString stringWithFormat:@"打%0.1f折",discountPercent];
        [mutableStr addAttributes:attributeDict range:NSMakeRange(0,str1.length)];
        
        NSDictionary *attributeDict2 = [NSDictionary dictionaryWithObjectsAndKeys:
                                        [UIFont systemFontOfSize:11.0],NSFontAttributeName,
                                        [UIColor colorWithRed:218/255.0 green:97/255.0 blue:34/255.0 alpha:1] ,NSForegroundColorAttributeName,[UIFont fontNamesForFamilyName:@"Arial"],NSFontAttributeName,
                                        nil];
        NSString *str2 = [NSString stringWithFormat:@"满%@元可用",[couponDic objectForKey:@"availablePrice"]];
        
        [mutableStr addAttributes:attributeDict2 range:NSMakeRange(str1.length+1,str2.length)];
        disLabel.attributedText = mutableStr;
        
    }
    
    dateLabel.text = [NSString stringWithFormat:@"%@-%@",[couponDic objectForKey:@"startUsingTime"],[couponDic objectForKey:@"expireTime"]];
    
    timeLabel.text = [NSString stringWithFormat:@"%@-%@",[couponDic objectForKey:@"dayStartUsingTime"],[couponDic objectForKey:@"dayEndUsingTime"]];
    
    if (self.isHave) {
        countLabel.text = [NSString stringWithFormat:@"X%@",[couponDic objectForKey:@"userCount"]];

    }else{
         countLabel.text = @"使用";
    }
    
    return cell;
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 93;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.couponList.count;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (self.isHave) {
        for (int i=0 ; i<self.seletype3.count; i++) {
            if (i == indexPath.row) {
                [self.seletype3 replaceObjectAtIndex:i withObject:@"1"];
                self.seleDic = [self.couponList objectAtIndex:i];
                
            }else{
                [self.seletype3 replaceObjectAtIndex:i withObject:@"0"];
                
            }
        }
        
        
        self.nbr = 1;
        
        [self.my_tableView reloadData];

    }else{
        NSDictionary *dic = [self.couponList objectAtIndex:indexPath.row];
        [self grabCoupon:dic];
        
    }

        
    

}



-(void)listUserCouponWhenPay{
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.couponType  forKey:@"couponType"];
    [params setObject:self.consumeAmount forKey:@"consumeAmount"];   //金额
    
    
    NSString* vcode = [gloabFunction getSign:@"listUserCouponWhenPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self.jsonPrcClient invokeMethod:@"listUserCouponWhenPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [wself.couponList addObjectsFromArray:responseObject];
        for (NSDictionary *dic in responseObject) {
            [wself.seletype3 addObject:@"0"];
        }
        if (wself.couponList.count==0) {
            [[NSNotificationCenter defaultCenter]postNotificationName:@"listUserNoGrabCouponWhenPay" object:nil];
             wself.isHave = NO;
        }else{
            wself.isHave = YES;
        }
         [wself.my_tableView reloadData];
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}
-(void)listUserNoGrabCouponWhenPay{
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.couponType  forKey:@"couponType"];
    [params setObject:self.consumeAmount forKey:@"consumeAmount"];   //金额
    
    
    NSString* vcode = [gloabFunction getSign:@"listUserNoGrabCouponWhenPay" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self.jsonPrcClient invokeMethod:@"listUserNoGrabCouponWhenPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        [wself.couponList addObjectsFromArray:responseObject];
        for (NSDictionary *dic in responseObject) {
            [wself.seletype3 addObject:@"0"];
        }
        if(wself.couponList.count==0){
            
            UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"商家没有满足条件的优惠券" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alerView show];
            [wself.navigationController popViewControllerAnimated:YES];
        }
            
        
        [wself.my_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}
#pragma mark 领取
-(void)grabCoupon:(NSDictionary *)dic{
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dic objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2"  forKey:@"sharedLvl"];   //默认2
    
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[dic objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
     
        if([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue]==50000 ){
            
            NSMutableDictionary *resultDic = [[NSMutableDictionary alloc]initWithDictionary:dic];
            [resultDic setObject:@"1" forKey:@"nbr"];
            
            if([self.couponType isEqualToString:@"3"]){
//                objectForKey:@"insteadPrice"
                self.disMoney = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"insteadPrice"]]floatValue];
            }else{
                self.disMoney = [self.consumeAmount floatValue]*[[NSString stringWithFormat:@"discountPercent"]floatValue];

            }
            
            [resultDic setObject:[NSString stringWithFormat:@"%.2f",self.disMoney] forKey:@"disMoney"];
            
            
            
            [[NSNotificationCenter defaultCenter]postNotificationName:@"refreshSeleCoupon" object:resultDic];
            [self.navigationController popViewControllerAnimated:YES];

        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

-(void)addNbr:(UIButton *)button{
    
     int useLimitedNbr = (int)[self.consumeAmount floatValue]/[[NSString stringWithFormat:@"%@",[self.seleDic objectForKey:@"availablePrice"]] floatValue];
    int limitedNbr = [[NSString stringWithFormat:@"%@",[self.seleDic objectForKey:@"userCount"]]intValue];
    
    if (button.tag == 77) {
       //减
        self.nbr = self.nbr-1;
        if (self.nbr <1) {
            self.nbr = 0;
        }
    }else{
        //加
        
        int use = useLimitedNbr>limitedNbr?limitedNbr:useLimitedNbr;
        
        self.nbr = self.nbr+1;
        
        if (self.nbr >= use) {
            self.nbr = use;
        }
        
    }
    self.disMoney = [[NSString stringWithFormat:@"%@",[self.seleDic objectForKey:@"insteadPrice"]]floatValue]*self.nbr;
    
    
    
    [self.my_tableView reloadData];
    
}

-(void)clickSubmit{
    
    
    
    
    for ( int i=0;i<self.seletype3.count;i++ ) {
        NSString *str = [self.seletype3 objectAtIndex:i];
        if ([str isEqualToString:@"1"]) {
             NSDictionary *couponDic =[self.couponList objectAtIndex:i];
            NSMutableDictionary *resultDic = [[NSMutableDictionary alloc]initWithDictionary:couponDic];
            [resultDic setObject:[NSString stringWithFormat:@"%d",self.nbr] forKey:@"nbr"];
            [resultDic setObject:[NSString stringWithFormat:@"%.2f",self.disMoney] forKey:@"disMoney"];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"refreshSeleCoupon" object:resultDic];
            
            [self.navigationController popViewControllerAnimated:YES];

            break;
        }
    }
}


@end
