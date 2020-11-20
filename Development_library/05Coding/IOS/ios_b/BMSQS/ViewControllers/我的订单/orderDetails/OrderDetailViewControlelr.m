//
//  OrderDetailViewControlelr.m
//  BMSQS
//
//  Created by gh on 15/12/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "OrderDetailViewControlelr.h"
#import "SVProgressHUD.h"
#import "OrderDetailTopCell.h"
#import "OrderRemarkCell.h"
#import "MJRefresh.h"

@interface OrderDetailViewControlelr ()<UIAlertViewDelegate, UIActionSheetDelegate>
{
    UITableView *tableview;
    
    BOOL isSelect;
    
    NSNumber * orderStat;//订单状态
    NSString * titleStr;// 标题
    NSNumber * isAgree;// 是否同意退款
    int    orderStatus;
    int    status;
    
    NSNumber * tableNumber;
    
}

@property (nonatomic,strong)NSMutableDictionary *mdataSource;
@property (nonatomic, strong)NSArray *productList; //产品列表
@property (nonatomic, strong)NSArray *statusArray;

@property (nonatomic, strong) UIView * detailView; //
@property (nonatomic, strong) NSArray * orderDataAry;//订单信息
@property (nonatomic, strong) NSArray * distributionAry;//配送信息
@property (nonatomic, strong) UIView  * heardViewOne;
@property (nonatomic, strong) UIView  * heardViewTwo;

@property (nonatomic, strong) UIView * sureOrderView;//确定台号视图
@property (nonatomic, strong) UITextField * inputField;

@end

@implementation OrderDetailViewControlelr


- (void)viewDidLoad {
    [super viewDidLoad];
    
    tableNumber = [NSNumber numberWithInt:0];
    
    [self setViewUp];
    
    [self makeSureTheOrder];

}

- (void)setViewUp {

    [self setNavBackItem];
    [self customRightBtn];
    
    tableview = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    tableview.dataSource = self;
    tableview.delegate = self;
    tableview.separatorStyle = UITableViewCellSeparatorStyleNone;

    [tableview addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [tableview addFooterWithTarget:self action:@selector(footerRereshing)];
    
    
    [self.view addSubview:tableview];
    
    [self setHeadView];
    
    [self getProductOrderInfo];
    
    
    

    
    
}

- (UIView *)heardViewOne{
    if (_heardViewOne == nil) {
        self.heardViewOne = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 80)];
        _heardViewOne.backgroundColor = UICOLOR(207, 207, 207, 1.0);
        
        UILabel * statementLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 15)];
        statementLB.font = [UIFont systemFontOfSize:12];
        statementLB.textColor = [UIColor redColor];
        statementLB.text = [NSString stringWithFormat:@"退款申请时间：%@", self.mdataSource[@"receivedTime"]];// receivedTime
        [self.heardViewOne addSubview:statementLB];
        
        UILabel * explainLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 15, APP_VIEW_WIDTH - 10, 30)];
        explainLB.font = [UIFont systemFontOfSize:12];
        explainLB.textColor = APP_TEXTCOLOR;
        explainLB.text = [NSString stringWithFormat:@"如果卖家不处理退款申请，默认24小时后订单关闭并且自动退款成功"];
        explainLB.numberOfLines = 0;
        [self.heardViewOne addSubview:explainLB];
        
        UILabel * remarkLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 45, APP_VIEW_WIDTH, 20)];
        remarkLB.font = [UIFont systemFontOfSize:12];
        remarkLB.numberOfLines = 0;
        remarkLB.textColor = APP_TEXTCOLOR;
        remarkLB.text = [NSString stringWithFormat:@"退款说明：%@", self.mdataSource[@"refundReason"]];
        [self.heardViewOne addSubview:remarkLB];
        
        
        UILabel * phoneLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 60, APP_VIEW_WIDTH, 15)];
        phoneLB.font = [UIFont systemFontOfSize:12];
        phoneLB.textColor = APP_TEXTCOLOR;
        phoneLB.text = [NSString stringWithFormat:@"备注信息：%@", self.mdataSource[@"refundRemark"]];
        [self.heardViewOne addSubview:phoneLB];
        
        
        
    }
    return _heardViewOne;
}
- (UIView *)heardViewTwo{
    if (_heardViewTwo == nil) {
        self.heardViewTwo = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 30)];
        _heardViewTwo.backgroundColor = UICOLOR(207, 207, 207, 1.0);
        
        UILabel * promptLB = [[UILabel alloc] initWithFrame:self.heardViewTwo.bounds];
        promptLB.textColor = [UIColor redColor];
        promptLB.font = [UIFont systemFontOfSize:12];
        promptLB.textAlignment = NSTextAlignmentCenter;
        promptLB.text = [NSString stringWithFormat:@"请在今天%@前确认接单",self.mdataSource[@"receiveTime"]];
        [self.heardViewTwo addSubview:promptLB];
    }
    return _heardViewTwo;
}



- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setImage:[UIImage imageNamed:@"order_more"] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

#pragma mark ------ 确认订单的台号
- (void)makeSureTheOrder
{
    self.sureOrderView = [[UIView alloc] initWithFrame:CGRectMake(20, APP_VIEW_ORIGIN_Y + 200, APP_VIEW_WIDTH - 40, 140)];
    _sureOrderView.backgroundColor = UICOLOR(207, 207, 207, 1.0);
    _sureOrderView.hidden = YES;
    
    UILabel * inputOrderLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 70, 50)];
    inputOrderLB.text = @"输入桌号:";
    inputOrderLB.font = [UIFont systemFontOfSize:15];
    inputOrderLB.textColor = UICOLOR(183, 34, 26, 1.0);
    [self.sureOrderView addSubview:inputOrderLB];
    
    self.inputField = [[UITextField alloc] initWithFrame:CGRectMake(100, 10, self.sureOrderView.frame.size.width - 110, 50)];
    _inputField.placeholder = @"请输入桌号:";
    _inputField.borderStyle = UITextBorderStyleRoundedRect;
    _inputField.keyboardType = UIKeyboardTypePhonePad;
    [self.sureOrderView addSubview:_inputField];
    
    UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
    [topView setBarStyle:UIBarStyleBlackTranslucent];
    
    UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(2, 5, 50, 25);
    [btn setTitle:@"隐藏" forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
    [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
    UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
    NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
    [topView setItems:buttonsArray];
    [_inputField setInputAccessoryView:topView];
    
    
    
    UIButton * sureBut = [UIButton buttonWithType:UIButtonTypeCustom];
    sureBut.frame = CGRectMake(10, 70, (self.sureOrderView.frame.size.width - 70) / 2, 50);
//    sureBut.backgroundColor = [UIColor grayColor];
    [sureBut setTitle:@"确定" forState:UIControlStateNormal];
    sureBut.layer.borderWidth = 1.5;
    sureBut.layer.cornerRadius = 4.5;
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGColorRef colorref = CGColorCreate(colorSpace,(CGFloat[]){ 1, 0, 0, 1 });
    [sureBut.layer setBorderColor:colorref];//边框颜色
    [sureBut addTarget:self action:@selector(sureButClick:) forControlEvents:UIControlEventTouchUpInside];
    [sureBut setTitleColor:UICOLOR(183, 34, 26, 1.0) forState:UIControlStateNormal];
    [self.sureOrderView addSubview:sureBut];
    
    UIButton * cancelBut = [UIButton buttonWithType:UIButtonTypeCustom];
    cancelBut.frame = CGRectMake(self.sureOrderView.frame.size.width - (self.sureOrderView.frame.size.width - 70) / 2 - 10, 70, (self.sureOrderView.frame.size.width - 70) / 2, 50);
//    cancelBut.backgroundColor = [UIColor grayColor];
    cancelBut.layer.borderWidth = 1.5;
    cancelBut.layer.cornerRadius = 4.5;
    [cancelBut.layer setBorderColor:colorref];//边框颜色
    [cancelBut addTarget:self action:@selector(cancelButClick:) forControlEvents:UIControlEventTouchUpInside];
    [cancelBut setTitle:@"取消" forState:UIControlStateNormal];
    [cancelBut setTitleColor:UICOLOR(183, 34, 26, 1.0) forState:UIControlStateNormal];
    [self.sureOrderView addSubview:cancelBut];
    
    [self.view addSubview:self.sureOrderView];
}





- (void)setHeadView {
    self.statusArray = [NSArray arrayWithObjects:@"待商家结算",@"未支付", @"支付中", @"已支付", @"已取消订单", @"支付失败", @"退款申请中", @"已退款", nil];
    self.orderDataAry = [NSArray arrayWithObjects:@"订单类型",@"支付状态",@"用户",@"电话",@"下单时间",@"订单号", nil];
    self.distributionAry = [NSArray arrayWithObjects:@"收货人",@"电话",@"地址", nil];
    NSArray *orderValueAry = [NSArray arrayWithObjects:@"orderType", @"status", @"receiver", @"receiverMobileNbr", @"orderTime", @"orderNbr", nil];
    NSArray *orderaAry = [NSArray arrayWithObjects:@"receiver", @"receiverMobileNbr", @"deliveryAddress", nil];
    
    self.detailView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 300)];
    
    UIView * mealView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 50)];
    [self.detailView addSubview:mealView];
    
    UILabel * mealLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH / 2 - 60, mealView.frame.origin.y - 1, 120, 50)];
    
        if (self.mdataSource) {
            
            NSString * Str = self.mdataSource[@"tableNbr"];
            
            if (![Str isEqualToString:@""] && ![Str isEqual:[NSNull null]]) {
                mealLabel.text = [NSString stringWithFormat:@"桌号：%@",self.mdataSource[@"tableNbr"]];
            }else{
                
                mealLabel.text = [NSString stringWithFormat:@"餐号：%@",self.mdataSource[@"mealNbr"]];
            }
            
        }
        
    
    

    
    mealLabel.font = [UIFont systemFontOfSize:20];
    [mealView addSubview:mealLabel];
    
    
    //
    UIView * orderView = [[UIView alloc] initWithFrame:CGRectMake(0, 50, APP_VIEW_WIDTH, 175)];
    orderView.backgroundColor = [UIColor whiteColor];
    [self.detailView addSubview:orderView];
    for (int i = 0; i < 6; i++) {
        UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(10, orderView.frame.origin.y - 50 + i * 25, 300, 25)];
        
        
        if (i == 0) {
            
            int orderType = [self.mdataSource[orderValueAry[i]] intValue];
            if (orderType == 10) {
                label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], @"其他订单"];
            }else if (orderType == 20){
                label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], @"堂食订单"];
            }else if (orderType == 21){
                label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], @"外卖订单"];
            }
            
        }else if (i == 1) {
            int ordType = [self.mdataSource[orderValueAry[i]] intValue];
            label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], self.statusArray[ordType]];
        }else{
            label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], self.mdataSource[orderValueAry[i]]];
        }
        
        
        
        
        
        label.font = [UIFont systemFontOfSize:12];
        [orderView addSubview:label];
    }
    UILabel *psLaebl = [[UILabel alloc] initWithFrame:CGRectMake(0, orderView.frame.origin.y - 50 + 6*25, orderView.frame.size.width, 25)];
    psLaebl.text = @"   配送信息";
    psLaebl.font = [UIFont systemFontOfSize:12];
    psLaebl.backgroundColor = APP_CELL_LINE_COLOR;
    [orderView addSubview:psLaebl];
    
    UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, orderView.frame.size.height-0.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_CELL_LINE_COLOR;
    [orderView addSubview:line];
    
    
    UIView * distributionView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 163, APP_VIEW_WIDTH, 75)];
    distributionView.backgroundColor = [UIColor whiteColor];
    [self.detailView addSubview:distributionView];
    
    for (int i = 0; i < 3; i++) {
        UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, distributionView.frame.origin.y - 225 + i * 25, 300, 25)];
        label.text = [NSString stringWithFormat:@"%@:%@",self.distributionAry[i], self.mdataSource[orderaAry[i]]];
        label.font = [UIFont systemFontOfSize:12];
        [distributionView addSubview:label];
    }
    UILabel *orderLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 300, APP_VIEW_WIDTH, 25)];
    orderLabel.backgroundColor = APP_CELL_LINE_COLOR;
    orderLabel.text = @"  订单详情";
    orderLabel.font = [UIFont systemFontOfSize:12];
    [self.detailView addSubview:orderLabel];
}



#pragma mark - UITableViewDelegate
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    if (section == 3) {
        return 10;
    }
    
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 25)];
    view.backgroundColor = APP_CELL_LINE_COLOR;
    return view;
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        return 325;
    }
    if (indexPath.section == 3 && indexPath.row == 1 ) {
        if (isSelect) {
            return 44+25*5;
        }
        
    }
    if (indexPath.section == 2) {
        
    }
    return 44;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 4;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 0) {
        return 1;
        
    }else if (section == 1) {
        return self.productList.count;
        
    }else if (section == 2) {
        return 1;
        
    } else if (section == 3) {
        return 3;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        NSString *cellIdentifier = @"topCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        
        [cell.contentView addSubview:self.detailView];
        return cell;
            
    }
    
    if (indexPath.section == 1) {
        OrderDetailTopCell *cell = (OrderDetailTopCell *)[tableView dequeueReusableCellWithIdentifier:@"orderCell"];
        if (cell == nil) {
            cell = [[OrderDetailTopCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"orderCell"];
            
        }
        UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, cell.frame.size.height-0.5, APP_VIEW_WIDTH, 0.5)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell.contentView addSubview:line];
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setCellValue:self.productList[indexPath.row]];
        
        return cell;
        
    }else if (indexPath.section == 2) {
     
        OrderRemarkCell *cell = [[OrderRemarkCell alloc] init];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//        cell.backgroundColor = [UIColor redColor];
        if (self.mdataSource) {
            [cell setCellValue:self.mdataSource[@"remark"]];

        }
        return cell;
        
        
    }else if (indexPath.section == 3) {
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, cell.frame.size.height-0.5, APP_VIEW_WIDTH, 0.5)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell.contentView addSubview:line];
        
        
        NSArray *array = [NSArray arrayWithObjects:@"消费金额", @"优惠金额", @"实际支付", nil];
        NSArray *amountArray = [NSArray arrayWithObjects:@"orderAmount",@"deduction",@"realPay", nil];
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH/2, 44)];
        label.font = [UIFont systemFontOfSize:12];
        [cell.contentView addSubview:label];
        label.text = array[indexPath.row];

        UILabel *rightLaebl = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-10, 44)];
        rightLaebl.font = [UIFont systemFontOfSize:12];
        rightLaebl.textAlignment = NSTextAlignmentRight;
        rightLaebl.text = @"";
        [cell.contentView addSubview:rightLaebl];
        if (self.mdataSource) {
            rightLaebl.text = [NSString stringWithFormat:@"%@元",self.mdataSource[amountArray[indexPath.row]]];
            
        }
        
        
        NSArray *deductionArray = [NSArray arrayWithObjects:@"优惠券", @"工银优惠", @"会员卡", @"商家红包", @"惠圈红包", nil];
        NSArray *deductionValueArray = [NSArray arrayWithObjects:@"couponDeduction", @"bankCardDeduction", @"cardDeduction", @"shopBonus", @"platBonus", nil];
        
        if (indexPath.row == 1  ) {
            rightLaebl.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2-20, 44);
            
            
            if ( !isSelect) {
                UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
                iv_arrow.backgroundColor = [UIColor clearColor];
                iv_arrow.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
                [cell.contentView addSubview:iv_arrow];
                
            }
        }
        
        if (indexPath.row == 1 && isSelect) {
            UIImageView *iv_arrow = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 19, 10, 7)];
            iv_arrow.backgroundColor = [UIColor clearColor];
            iv_arrow.image = [UIImage imageNamed:@"iv_arrowTopHeight"];
            [cell.contentView addSubview:iv_arrow];
            
            for (int i=0; i<5; i++) {
                
                
                
                UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                lb_left.font = [UIFont systemFontOfSize:11];
                lb_left.textColor = [UIColor darkGrayColor];
                [cell.contentView addSubview:lb_left];
                lb_left.text = deductionArray[i];
                if (i ==0) {
                    
                }
                
                
                
                UILabel *lb_right = [[UILabel alloc] initWithFrame:CGRectMake(20, 44+25*i, APP_VIEW_WIDTH-(20*2), 20)];
                lb_right.textAlignment = NSTextAlignmentRight;
                lb_right.backgroundColor = [UIColor clearColor];
                lb_right.font = [UIFont systemFontOfSize:11];
                lb_right.textColor = [UIColor darkGrayColor];
                [cell.contentView addSubview:lb_right];
                if (self.mdataSource) {
                    lb_right.text = [NSString stringWithFormat:@"%@元",self.mdataSource[deductionValueArray[i]]];
                    
                }
                
                
            }
            
        }
        
        return cell;
        
    }
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 3 && indexPath.row == 1) {
        isSelect = !isSelect;
        [tableView reloadRowsAtIndexPaths:@[indexPath]
                           withRowAnimation:UITableViewRowAnimationFade];
        
        
    }
    
    
}


- (void)itemClick:(UIButton *)sender
{
    NSLog(@"%@", titleStr);
    if ([titleStr isEqualToString:@"待接单"]) {
        UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles:@"撤销订单",@"确认订单", nil];
        action.tag = 2004;
        [action showInView:self.view];
        
    }else if ([titleStr isEqualToString:@"配送中"] || [titleStr isEqualToString:@"待付款"]){
        
         if (status == 0 || status == 1 || status == 5){
            UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles:@"撤销订单",@"去结算", nil];
            action.tag = 2103;
            [action showInView:self.view];
            
        }else{
            
            UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles:@"撤销订单", nil];
            action.tag = 2003;
            [action showInView:self.view];
        }
        
        
        
    }else if ([titleStr isEqualToString:@"退款申请中"]){
    
        UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles:@"同意退款",@"拒绝退款" ,nil];
        action.tag = 2002;
        [action showInView:self.view];
        
    }else{
        
        UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles: nil];
        action.tag = 2001;
        [action showInView:self.view];
    }

    
}
#pragma mark ---- UIActionSheetDelegate,

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
        switch (buttonIndex) {
            case 0:{
                NSLog(@"联系用户");
                UIAlertView * alertView1 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"你确定拨打电话！" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
                alertView1.tag = 5001;
                [alertView1 show];
            }
                
                break;
                
            case 1:{
                NSLog(@"撤销订单");
                if (actionSheet.tag == 2001) {
                    break;
                }else if (actionSheet.tag == 2004 || actionSheet.tag == 2003 || actionSheet.tag == 2103){
                    UIAlertView * alertView2 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"确定要撤销订单吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
                    alertView2.tag = 5002;
                    [alertView2 show];
                }else if (actionSheet.tag == 2002){
                    UIAlertView * alertView2 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"确定要退款吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
                    alertView2.tag = 5011;
                    [alertView2 show];
                }
                
            }
                break;
                
            case 2:{
                
                if (actionSheet.tag == 2001 || actionSheet.tag == 2003) {
                    break;
                }else if (actionSheet.tag == 2004){
                    NSLog(@"确认接单");
                    UIAlertView * alertView3 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"确定要接订单吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
                    alertView3.tag = 5003;
                    [alertView3 show];
                }else if (actionSheet.tag == 2002){
                    NSLog(@"拒绝退款");
                    UIAlertView * alertView3 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"确定拒绝退款吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
                    alertView3.tag = 5012;
                    [alertView3 show];
                }else if (actionSheet.tag == 2103){
                    NSLog(@"去结算");
                    
                    SettlementViewController * settlementVC = [[SettlementViewController alloc] init];
                    settlementVC.orderCode = self.mdataSource[@"orderCode"];
                    settlementVC.actualOrderAmount = self.mdataSource[@"actualOrderAmount"];
                    NSArray * array = self.mdataSource[@"productList"];
                    settlementVC.orderProductList = [@[] mutableCopy];
                    for (NSDictionary * dic in array) {
                        NSNumber * number = dic[@"orderProductId"];
                        [settlementVC.orderProductList addObject:number];
                    }
                    settlementVC.productList = [NSArray arrayWithArray:self.productList];
                    settlementVC.payTypeNum = self.mdataSource[@"status"];
                    [self.navigationController pushViewController:settlementVC animated:YES];
                }
                
            }
                break;
                
            default:
                break;

       }
}


#pragma mark ----  UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:
            
            break;
        case 1:{
            NSLog(@"确定联系用户");
            if (alertView.tag == 5001) {
                // 方法1:
                UIWebView *callWebView = [[UIWebView alloc] init];
                
                NSNumber * number = self.mdataSource[@"receiverMobileNbr"];
                NSURL *telURL = [NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",number]];
                
                [callWebView loadRequest:[NSURLRequest requestWithURL:telURL]];
                [self.view addSubview:callWebView];
                
                
            }else if (alertView.tag == 5002){
                NSLog(@"撤销订单");
        
                orderStat = [NSNumber numberWithInt:24];
                [self updateProductOrderStatus];
                [self setViewUp];
                
            }else if (alertView.tag == 5003){
                NSLog(@"确定接单");
                
                int tableSwitch = [(NSNumber *)self.mdataSource[@"tableNbrSwitch"] intValue];
                int orderType = [(NSNumber *)self.mdataSource[@"orderType"] intValue];
                if (tableSwitch == 1 && orderType == 20){
                    
                self.sureOrderView.hidden = NO;
                    
                    
                }else{
                    
                    orderStat = [NSNumber numberWithInt:21];
                    [self updateProductOrderStatus];
                    [self setViewUp];
                }
                
                
            }else if (alertView.tag == 5011){
                NSLog(@"确定要退款");
                isAgree = [NSNumber numberWithInt:1];
                [self dealRefund];
                [self setViewUp];
                
            }else if (alertView.tag == 5012){
                NSLog(@"拒绝退款");
                isAgree = [NSNumber numberWithInt:0];
                [self dealRefund];
                [self setViewUp];
                
            }
            
        }
            
            break;
            
        default:
            break;
    }
}




#pragma mark - http request
- (void)getProductOrderInfo
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.orderCode forKey:@"orderCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getProductOrderInfo" strParams:self.orderCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:self.eatPayType forKey:@"eatPayType"];
    

    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getProductOrderInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        self.mdataSource = [NSMutableDictionary dictionaryWithDictionary:responseObject];
        self.productList = [responseObject objectForKey:@"productList"];
        [self setHeadView];
        [tableview reloadData];
        
         status = [self.mdataSource[@"status"] intValue];
         orderStatus = [self.mdataSource[@"orderStatus"] intValue];
        if (orderStatus == 20) {
            titleStr = @"待接单";
        }else if (orderStatus == 25){
            titleStr = @"待接单";
        }else if (orderStatus == 23 && status == 3){
            titleStr = @"交易成功";
        }else if ((orderStatus == 23 && status == 1) ||(orderStatus == 23 && status == 5) || (orderStatus == 23 && status == 0)){
            titleStr = @"待付款";
        }else if (orderStatus == 22){
            titleStr = @"配送中";
        }else if (orderStatus == 24){
            titleStr = @"交易取消";
        }else if (orderStatus == 21){
            titleStr = @"配送中";
        }
        
        
        if (status == 6){
            titleStr = @"退款申请中";
        }else if (status == 7){
            titleStr = @"订单已关闭";
        }
        
        
        
        [self setNavTitle:titleStr];
        if ([titleStr isEqualToString:@"退款申请中"]) {
            tableview.tableHeaderView = self.heardViewOne;
        }else if ([titleStr isEqualToString:@"待接单"]){
            tableview.tableHeaderView = self.heardViewTwo;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];
    
}
// 改变订单状态，撤销订单，确认接单
- (void)updateProductOrderStatus
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.orderCode forKey:@"orderCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"updateProductOrderStatus" strParams:self.orderCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:orderStat forKey:@"orderStatus"];//订单状态
    [params setObject:self.mdataSource[@"status"] forKey:@"status"];// 支付状态
    
    if (tableNumber == [NSNumber numberWithInt:0]) {
        [params setObject:@"" forKey:@"tableNbr"];
    }else{
        [params setObject:tableNumber forKey:@"tableNbr"];
    }
    
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"updateProductOrderStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
     
        NSLog(@"%@", responseObject);
        int number = [(NSNumber *)responseObject[@"code"] intValue];
        if (number == 50000) {
            NSLog(@"修改成功");
        }else if (number == 20000){
            NSLog(@"修改失败");
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

    
    
    
}

// 同意退款或者拒绝退款
- (void)dealRefund
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.orderCode forKey:@"orderCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"dealRefund" strParams:self.orderCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:isAgree forKey:@"isAgree"];
    
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"dealRefund" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

}

//#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    [tableview headerEndRefreshing];
    [self setViewUp];
//    [self getProductOrderInfo];
}
- (void)footerRereshing{
    [tableview footerEndRefreshing];
    [self setViewUp];
//    [self getProductOrderInfo];
}

#pragma mark ---- sureButClick cancelButClick
- (void)sureButClick:(UIButton *)sender
{
    NSLog(@"确定");
    
    tableNumber = (NSNumber *)self.inputField.text;
    
    if ([self.inputField.text isEqualToString:@""] || [self.inputField.text length] == 0) {
        
        UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"请输入桌号" message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:nil, nil];
        [alertView show];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [alertView dismissWithClickedButtonIndex:0 animated:YES];
        });
        
    }else{
        tableview.alpha = 0.4;
        
        self.sureOrderView.hidden = YES;
        
        orderStat = [NSNumber numberWithInt:21];
        
        
        [self updateProductOrderStatus];
        [self setViewUp];
        
    }
    
}

- (void)cancelButClick:(UIButton *)sender
{
    NSLog(@"取消");
    self.sureOrderView.hidden = YES;
    
}
// 隐藏按钮
- (void)dismissKeyB {
    
    [self.inputField resignFirstResponder];
//    tableNumber = (NSNumber *)self.inputField.text;
}

@end
