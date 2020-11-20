//
//  BMSQ_BuyCouponViewController.m
//  BMSQC
//
//  Created by liuqin on 15/12/23.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_BuyCouponViewController.h"
#import "BMSQ_PayCardViewController.h"
#import "MobClick.h"
@interface BMSQ_BuyCouponViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate>

@property (nonatomic, strong)UITableView *m_tableView;
@property (nonatomic, assign)BOOL isHaveData;

@property (nonatomic, strong)NSDictionary *resultDic;

@property (nonatomic, assign)int nbrPerPerson;  //可购买张数
@property (nonatomic, strong)NSString *remark;  //使用说明


@property (nonatomic, strong)NSString *shopName;
@property (nonatomic, strong)NSString *shopCode;


@property (nonatomic, assign)float consumAmount; //花费总额
@property (nonatomic, assign)float insteadPrice;  //面值
@property (nonatomic, assign)int  count; //可购买张数
@property (nonatomic, assign)int  useCount;//用户购买张数


@property (nonatomic, assign)BOOL canUsePlatBonus; //是否有惠圈红包
@property (nonatomic, assign)BOOL canUseShopBonus; //是否有商家红包
@property (nonatomic, assign)float platBonus;  //惠圈红包
@property (nonatomic, assign)float shopBonus;  //商家红包

@property (nonatomic, assign)NSString *usePlatBonus;  //惠圈红包
@property (nonatomic, strong)NSString *useShopBonus;  //商家红包

@property (nonatomic, assign)BOOL isAcceptBankCard;//有没有开通工行卡支付  0元1有
@property (nonatomic, assign)float minRealPay;  //最少消费金额


@end


@implementation BMSQ_BuyCouponViewController
-(void)keyboardHide{
    
    [UIView animateWithDuration:0.2 animations:^{
        self.m_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    } completion:^(BOOL finished) {
        
    }];
    
    
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"BuyCouponView"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"BuyCouponView"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    [super setNavigationBar];
    [super setNavTitle:@"支付详情"];
    [super setNavBackItem];
    
    self.useCount = 1;
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardHide)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(calculate) name:@"calculate" object:nil];
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    self.m_tableView.delegate = self;
    self.m_tableView.dataSource = self;
    [self.view insertSubview:self.m_tableView belowSubview:self.navigationView];
    
    [self getInfoWhenCouponPay];
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (section ==3) {
        if (self.nbrPerPerson ==1) {
            return 0;
        }else{
            return 1;
        }
    }
    else if (section ==6){   //惠圈红包
        
        if (self.canUsePlatBonus) {
            return 1;
        }else{
            return 0;
        }
    }
    else if (section ==7){   //商家红包
        
        if (self.canUseShopBonus) {
            return 1;
        }else{
            return 0;
        }
        
    }else if (section ==8){   //工行卡折扣 没有
        return 0;
        
    }
    return 1;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.isHaveData? 10:0;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.section == 9)
        return 150;
    else if(indexPath.section == 4){
        CGSize size = [self.remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                              options:NSStringDrawingUsesLineFragmentOrigin
                                           attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                              context:nil].size;
        
        return size.height >30?30+size.height:60;
    }
    else
        return 50;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        static NSString *identifier = @"BuyCouponSection0cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
            label.text = @"商家名称";
            label.textColor = APP_TEXTCOLOR;
            label.font = [UIFont systemFontOfSize:13];
            [bgView addSubview:label];
            
            UILabel *shopNameLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 0,APP_VIEW_WIDTH-90, 45)];
            shopNameLabel.text = @"商家名称";
            shopNameLabel.textColor = APP_TEXTCOLOR;
            shopNameLabel.textAlignment = NSTextAlignmentRight;
            shopNameLabel.font = [UIFont systemFontOfSize:13];
            shopNameLabel.tag = 100;
            [bgView addSubview:shopNameLabel];
            
        }
        UILabel *shopNameLabel = (UILabel *)[cell viewWithTag:100];
        NSDictionary *batchCouponInfo = [self.resultDic objectForKey:@"batchCouponInfo"];
        shopNameLabel.text =[NSString stringWithFormat:@"%@",[batchCouponInfo objectForKey:@"shopName"]] ;
        
        return cell;
    }
   else if (indexPath.section ==1) {
        static NSString *identifier = @"BuyCouponcell1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
            label.text = @"兑换券";
            label.textColor = APP_TEXTCOLOR;
            label.font = [UIFont systemFontOfSize:13];
            label.tag = 100;
            [bgView addSubview:label];
            
            UILabel *priceLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-90, 0,80, 45)];
            priceLabel.text = @"1000元";
            priceLabel.textColor = APP_TEXTCOLOR;
            priceLabel.textAlignment = NSTextAlignmentRight;
            priceLabel.font = [UIFont systemFontOfSize:13];
            priceLabel.backgroundColor = [UIColor clearColor];
            priceLabel.tag = 200;
            [bgView addSubview:priceLabel];
            
            UILabel *fountionLabel = [[UILabel alloc]initWithFrame:CGRectMake(60, 0,APP_VIEW_WIDTH-70-90, 45)];
            fountionLabel.text = @"fountionLabelfountionLabelfountionLabelfountionLabelfountionLabelfountionLabel";
            fountionLabel.textColor = APP_TEXTCOLOR;
            fountionLabel.font = [UIFont systemFontOfSize:13];
            fountionLabel.backgroundColor = [UIColor clearColor];
            fountionLabel.tag = 300;
            [bgView addSubview:fountionLabel];
        }
       
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       UILabel *priceLabel = (UILabel *)[cell viewWithTag:200];
       UILabel *fountionLabel = (UILabel *)[cell viewWithTag:300];
       NSDictionary *batchCouponInfo = [self.resultDic objectForKey:@"batchCouponInfo"];
       NSString *couponType =[NSString stringWithFormat:@"%@",[batchCouponInfo objectForKey:@"couponType"]];
       if ([couponType intValue] == 7) {
           label.text = @"兑换券";
       }else if ([couponType intValue] == 8){
           label.text = @"代金券";
       }else{
           label.text = @"优惠券";
       }
       priceLabel.text = [NSString stringWithFormat:@"%.2f元",self.insteadPrice];
       fountionLabel.text = [NSString stringWithFormat:@"%@",[batchCouponInfo objectForKey:@"function"]];
       
       
        return cell;
    }
   else if (indexPath.section ==2) {
       static NSString *identifier = @"BuyCouponcell2";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
           label.text = @"数量";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           [bgView addSubview:label];
           
           
           UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH-90, 45)];
           countLabel.textColor = APP_TEXTCOLOR;
           countLabel.font = [UIFont systemFontOfSize:13];
           countLabel.tag = 100;
           countLabel.textAlignment = NSTextAlignmentRight;
           countLabel.text = @"1张";
           [bgView addSubview:countLabel];
           countLabel.hidden = YES;
           
           
           UIView *countView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, 0,120, 45)];
           countView.backgroundColor = [UIColor clearColor];
           countView.tag = 200;
           [bgView addSubview:countView];
           
           UIButton *LessLabel =[[UIButton alloc]initWithFrame:CGRectMake(7,10, 25, 25)];
           LessLabel.layer.borderWidth=1;
           LessLabel.layer.borderColor = [APP_NAVCOLOR CGColor];
           [LessLabel setTitle:@"-" forState:0];
           [LessLabel setTitleColor:APP_NAVCOLOR forState:0];
           LessLabel.tag = 100;
           [countView addSubview:LessLabel];
           
           
           
           UIButton *addLabel =[[UIButton alloc]initWithFrame:CGRectMake(85,10, 25, 25)];
           addLabel.layer.borderWidth=1;
           addLabel.layer.borderColor = [APP_NAVCOLOR CGColor];
           [addLabel setTitle:@"+" forState:0];
           [addLabel setTitleColor:APP_NAVCOLOR forState:0];
           addLabel.tag = 200;
           [countView addSubview:addLabel];
           
           [LessLabel addTarget:self action:@selector(changeCountMothed:) forControlEvents:UIControlEventTouchUpInside];
           [addLabel addTarget:self action:@selector(changeCountMothed:) forControlEvents:UIControlEventTouchUpInside];

           
           UILabel *countLabel1 = [[UILabel alloc]initWithFrame:CGRectMake(25+7, 0, 85-25, 45)];
           countLabel1.textColor = APP_TEXTCOLOR;
           countLabel1.font = [UIFont systemFontOfSize:13];
           countLabel1.textAlignment = NSTextAlignmentCenter;
           countLabel1.text = @"1";
           countLabel1.tag = 300;
           [countView addSubview:countLabel1];
           
           countView.hidden = YES;
           
       }
       
       
       
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       UIView *countView = (UIView *)[cell viewWithTag:200];
       UILabel *label1 = (UILabel *)[cell viewWithTag:300];

       if (self.nbrPerPerson ==1) {
           label.hidden = NO;
           countView.hidden = YES;
       }else{
           label.hidden = YES;
           countView.hidden = NO;
           label1.text = [NSString stringWithFormat:@"%d",self.useCount];
       }

       return cell;
   }
    
   else if (indexPath.section ==3) {
       static NSString *identifier = @"BuyCouponcell3";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 45)];
           label.text = @"您可以尽情购买优惠券";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           label.textAlignment = NSTextAlignmentRight;
           label.tag = 100;
           [bgView addSubview:label];
        }
       
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       if (self.nbrPerPerson ==0) {
           label.text =@"您可以尽情购买优惠券";
       }else{
           label.text = [NSString stringWithFormat:@"您可以购买%d张",self.nbrPerPerson];// @"您可以心情购买优惠券";

       }
       
       
       
       
       return cell;
   }
   else if (indexPath.section == 4) {
       static NSString *identifier = @"BuyCouponcell4";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 55)];
           bgView.backgroundColor = [UIColor whiteColor];
           bgView.tag = 200;
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 55)];
           label.text = @"优惠券说明 ";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           label.tag = 100;
           label.numberOfLines = 0;
           [bgView addSubview:label];
       }
       
       CGSize size = [self.remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                               context:nil].size;
       
//       return size.height >30?30+size.height:60;
       UIView *bgView = [cell viewWithTag:200];
       bgView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, size.height >25?25+size.height:25);
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       label.frame = CGRectMake(10, 0, APP_VIEW_WIDTH-20, size.height >25?25+size.height:25);
       label.text = [NSString stringWithFormat:@"优惠券说明 \n%@",self.remark];
       
       return cell;
   }
   else if (indexPath.section ==5) {
       static NSString *identifier = @"BuyCouponcell4";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
           label.text = @"支付";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           [bgView addSubview:label];
           
           UILabel *payLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 0,APP_VIEW_WIDTH-90, 45)];
           payLabel.text = @"12元";
           payLabel.textColor = APP_NAVCOLOR;
           payLabel.textAlignment = NSTextAlignmentRight;
           payLabel.font = [UIFont systemFontOfSize:13];
           payLabel.tag = 100;
           [bgView addSubview:payLabel];
       }
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       label.text = [NSString stringWithFormat:@"%0.2f元",self.consumAmount];

       
       return cell;
   }
   else if (indexPath.section ==6) {
       static NSString *identifier = @"BuyCouponcell6";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
           label.text = @"惠圈红包";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           [bgView addSubview:label];
           
           UILabel *payLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0,180, 45)];
           payLabel.text = @"元 惠圈红包可用100.00元";
           payLabel.textColor = APP_TEXTCOLOR;
           payLabel.textAlignment = NSTextAlignmentRight;
           payLabel.font = [UIFont systemFontOfSize:12];
           payLabel.tag = 200;
           [bgView addSubview:payLabel];
           
           
           UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(65, 0, APP_VIEW_WIDTH-65-190, 45)];
           textField.placeholder = @"输入金额";
           textField.textAlignment = NSTextAlignmentCenter;
           textField.font = [UIFont systemFontOfSize:11];
           textField.backgroundColor = [UIColor clearColor];
           textField.textColor = APP_NAVCOLOR;
           textField.returnKeyType = UIReturnKeyDone;
           textField.keyboardType = UIKeyboardTypeNumberPad;
           textField.tag = 100;
           textField.delegate = self;
           [textField addTarget:self action:@selector(changeTextField:) forControlEvents:UIControlEventEditingChanged];
           [bgView addSubview:textField];
       }
       
       UILabel *label = (UILabel *)[cell viewWithTag:200];
       label.text = [NSString stringWithFormat:@"元 惠圈红包可用%0.2f元",self.platBonus];

       UITextField *textField = (UITextField *)[cell viewWithTag:100];
       textField.text =self.usePlatBonus<=0?@"":self.usePlatBonus;

       return cell;
   }
   else if (indexPath.section ==7) {
       static NSString *identifier = @"BuyCouponcell7";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
           label.text = @"商家红包";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           [bgView addSubview:label];
           
           UILabel *payLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-150, 0,140, 45)];
           payLabel.text = @"元 商家红包可用100.00元";
           payLabel.textColor = APP_TEXTCOLOR;
           payLabel.textAlignment = NSTextAlignmentRight;
           payLabel.font = [UIFont systemFontOfSize:12];
           payLabel.tag = 100;
           [bgView addSubview:payLabel];
           
           UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(65, 0, APP_VIEW_WIDTH-65-150, 45)];
           textField.placeholder = @"输入金额";
           textField.textAlignment = NSTextAlignmentCenter;
           textField.font = [UIFont systemFontOfSize:11];
           textField.backgroundColor = [UIColor clearColor];
           textField.textColor = APP_NAVCOLOR;
           textField.returnKeyType = UIReturnKeyDone;
           textField.keyboardType = UIKeyboardTypeNumberPad;
           textField.tag = 200;
           [textField addTarget:self action:@selector(changeTextField:) forControlEvents:UIControlEventEditingChanged];
           textField.delegate = self;
           [bgView addSubview:textField];
       }
       UITextField *textField = (UITextField *)[cell viewWithTag:200];
       
       textField.text =self.useShopBonus.length ==0?@"":self.useShopBonus;
       
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       label.text = [NSString stringWithFormat:@"元 商家红包可用%0.2f元",self.shopBonus];

       
       return cell;
   }
   else if (indexPath.section ==8) {
       static NSString *identifier = @"BuyCouponcell8";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
           bgView.backgroundColor = [UIColor whiteColor];
           [cell addSubview:bgView];
           
           UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 80, 45)];
           label.text = @"工行折扣";
           label.textColor = APP_TEXTCOLOR;
           label.font = [UIFont systemFontOfSize:13];
           [bgView addSubview:label];
           
           UILabel *payLabel = [[UILabel alloc]initWithFrame:CGRectMake(100, 0,APP_VIEW_WIDTH-90, 45)];
           payLabel.text = @"5.4折";
           payLabel.textColor = APP_NAVCOLOR;
           payLabel.font = [UIFont systemFontOfSize:14];
           [bgView addSubview:payLabel];
       }
       UILabel *label = (UILabel *)[cell viewWithTag:100];
       
       
       
       label.text = [NSString stringWithFormat:@"元 商家红包可用%0.2f元",self.platBonus];
       return cell;
   }
   else if (indexPath.section ==9){
       static NSString *identifier = @"BuyCouponcell";
       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
       if (cell == nil) {
           cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
           cell.selectionStyle = UITableViewCellSelectionStyleNone;
           cell.backgroundColor = [UIColor clearColor];
           UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 50)];
           button.backgroundColor = APP_NAVCOLOR;
           [button setTitle:@"使用惠支付" forState:UIControlStateNormal];
           [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
           button.layer.cornerRadius = 4;
           button.layer.masksToBounds = YES;
           [cell addSubview:button];
           [button addTarget:self action:@selector(clickPay) forControlEvents:UIControlEventTouchUpInside];
       }
       return cell;
   }
    
    static NSString *identifier = @"BuyCouponcell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 50)];
        button.backgroundColor = APP_NAVCOLOR;
        [button setTitle:@"使用惠支付" forState:UIControlStateNormal];
        [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        button.layer.cornerRadius = 4;
        button.layer.masksToBounds = YES;
        [cell addSubview:button];
        [button addTarget:self action:@selector(clickPay) forControlEvents:UIControlEventTouchUpInside];
    }
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:6];
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:index];
    UITextField *textField = (UITextField *)[cell viewWithTag:100];
    [textField resignFirstResponder];

    index = [NSIndexPath indexPathForRow:0 inSection:7];
    cell = [tableView cellForRowAtIndexPath:index];
    textField = (UITextField *)[cell viewWithTag:200];
    [textField resignFirstResponder];
}


-(void)textFieldDidBeginEditing:(UITextField *)textField{

    [UIView animateWithDuration:0.2 animations:^{
        self.m_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y-150, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    } completion:^(BOOL finished) {
        
    }];


}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [UIView animateWithDuration:0.2 animations:^{
        self.m_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    } completion:^(BOOL finished) {
        
    }];
    
    
    [textField resignFirstResponder];
    return YES;
}
-(void)changeTextField:(UITextField *)textField{
     int i= (int)textField.tag;
    if (i == 100) {  //惠圈
        float f = [textField.text floatValue];
        if( f <= self.platBonus){
            self.usePlatBonus = textField.text;
        }else{
            self.usePlatBonus = [NSString stringWithFormat:@"%0.2f",self.platBonus];
            NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:6];
            [self.m_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        }
            
       

    }else if(i == 200){ //商家
        float f =[textField.text floatValue];
        if( f <= self.shopBonus){
            self.useShopBonus =textField.text;
        }else{
            self.useShopBonus =[NSString stringWithFormat:@"%0.2f",self.shopBonus];
            NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:7];
            [self.m_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        }
        
      

    }
    

    
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"calculate" object:nil];
    
}

#pragma mark 计算支付金额
-(void)calculate{
    
    self.consumAmount = self.insteadPrice*self.useCount-[self.useShopBonus floatValue]-[self.usePlatBonus floatValue];
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:5];
    [self.m_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    
}

-(void)changeCountMothed:(UIButton *)button{
    
    int i = (int)button.tag;
    if (i == 200) {
        self.useCount = self.useCount + 1;
        if(self.nbrPerPerson!=0){
            if (self.useCount >=self.nbrPerPerson) {
                self.useCount = self.nbrPerPerson;
            }
        }
    }else if (i == 100){
        self.useCount = self.useCount - 1;
        if (self.useCount<=1){
            self.useCount = 1;
        }
        
    }
   
    [[NSNotificationCenter defaultCenter]postNotificationName:@"calculate" object:nil];
    
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
    [self.m_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];

}

#pragma mark --- 优惠券详情
-(void)getInfoWhenCouponPay{
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.batchCouponCode forKey:@"batchCouponCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getInfoWhenCouponPay" strParams:self.batchCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"getInfoWhenCouponPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        wself.isHaveData = YES;
        wself.resultDic = responseObject;
        NSDictionary *dic = [responseObject objectForKey:@"batchCouponInfo"];
        wself.nbrPerPerson = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"nbrPerPerson"]]intValue];
        wself.remark = [dic objectForKey:@"remark"];
        wself.shopName = [dic objectForKey:@"shopName"];
        wself.shopCode = [dic objectForKey:@"shopCode"];
        NSString *insteadPrice = [NSString stringWithFormat:@"%@元",[dic objectForKey:@"payPrice"]];
        wself.insteadPrice = [insteadPrice floatValue];
        wself.consumAmount = wself.insteadPrice *1;
        NSDictionary *bonus = [responseObject objectForKey:@"bonus"];
        wself.canUsePlatBonus = [[NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUsePlatBonus"]]intValue]==1?YES:NO;
        wself.canUseShopBonus = [[NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUseShopBonus"]]intValue]==1?YES:NO;
        wself.platBonus =[[NSString stringWithFormat:@"%@",[bonus objectForKey:@"platBonus"]]floatValue];
        wself.shopBonus =[[NSString stringWithFormat:@"%@",[bonus objectForKey:@"shopBonus"]]floatValue];
        
        wself.isAcceptBankCard = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"isAcceptBankCard"]]intValue];
        
        
        [wself.m_tableView reloadData];
        
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"请求错误 "];
        
        
    }];
    
    
}

#pragma mark --- 购买
-(void)clickPay{
    
    if (!self.isAcceptBankCard) {
        
        UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"您还没有开通工行卡支付哟" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alertView show];
        
        return;
    }
    
    BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
    vc.fromVC =(int) self.navigationController.viewControllers.count-1;
    vc.shopCode = self.shopCode;
    vc.consumeAmount = [NSString stringWithFormat:@"%0.2f",self.consumAmount]; //花费钱数
    vc.batchCouponCode = self.batchCouponCode;
    vc.nbrCoupon = [NSString stringWithFormat:@"%d",self.useCount];  //优惠张数
    vc.platBonus = self.usePlatBonus.length>0?self.usePlatBonus:@"0";
    vc.shopBonus = self.useShopBonus.length>0?self.useShopBonus:@"0";
    vc.payType = @"1";   //支付方式
    vc.shopName = self.shopName;
    vc.isTakeout = NO;
    vc.isdining = NO;
    vc.isBuy = YES;

    [self.navigationController pushViewController:vc animated:YES];
}
@end
