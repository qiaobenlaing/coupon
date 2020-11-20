//
//  BMSQ_TrainViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/28.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_TrainViewController.h"


@interface BMSQ_TrainViewController ()<UITableViewDataSource,UITableViewDelegate,TrainCellDelegate>


@property (nonatomic, strong)UITableView *trainTableView;
@property (nonatomic, strong)NSMutableArray *signClassList;

@end


@implementation BMSQ_TrainViewController


-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"我的培训报名"];
    
    self.signClassList = [[NSMutableArray alloc]init];
    
    self.trainTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.trainTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.trainTableView.backgroundColor = [UIColor clearColor];
    self.trainTableView.delegate =self;
    self.trainTableView.dataSource =self;
    
    [self.view addSubview:self.trainTableView];
    
    [self getUserSignClassList];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.signClassList.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    

    NSDictionary *trainDic = [self.signClassList objectAtIndex:indexPath.row];
    NSString *handMemo=[NSString stringWithFormat:@"学校回复 : %@",[trainDic objectForKey:@"handMemo"]];
    CGSize size = [handMemo boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-40, MAXFLOAT)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12]}
                                         context:nil].size;
    
    
    return 4*40+10 + (size.height>40?size.height+10:40);
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"trainCell";
    TrainCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[TrainCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.trainDelegate = self;
    }
    cell.trainDic = [self.signClassList objectAtIndex:indexPath.row];
    [cell setTrainCell:[self.signClassList objectAtIndex:indexPath.row]];
    return cell;
    
    
}

-(void)payTrain:(NSDictionary *)trainDic{
    NSLog(@"-->%@",trainDic);
}


-(void)getUserSignClassList{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:@"-1" forKey:@"feeFlag"];
    [params setObject:@"-1" forKey:@"handFlag"];
    [params setObject:@"1" forKey:@"page"];  	//默认从1开始，依次递增
    [params setObject:@"" forKey:@"shopCode"];
    [params setObject:@"" forKey:@"classCode"];

    
    NSString* vcode = [gloabFunction getSign:@"getUserSignClassList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    [self.jsonPrcClient invokeMethod:@"getUserSignClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        [weakSelf.signClassList addObjectsFromArray: [responseObject objectForKey:@"signClassList"]];
        
        [weakSelf.trainTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}
@end



@interface TrainCell ()

@property (nonatomic, strong)UIView *bgView;

@property (nonatomic, strong)UIView *firstView;
@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)UILabel *classLabel;

@property (nonatomic, strong)UIView *phoneView;
@property (nonatomic, strong)UILabel *phoneLabel;

@property (nonatomic, strong)UIView *timeView;
@property (nonatomic, strong)UILabel *timeLabel;

@property (nonatomic, strong)UIView *replayView;
@property (nonatomic, strong)UILabel *replayLabel;

@property (nonatomic, strong)UIView *feeView;
@property (nonatomic, strong)UILabel *feeLabel;

@property (nonatomic, strong)UIButton *payButton;

@end

@implementation TrainCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        self.bgView = [[UIView alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 200)];
        self.bgView.backgroundColor = [UIColor clearColor];
        [self addSubview:self.bgView];
        self.bgView.layer.borderColor = [APP_VIEW_BACKCOLOR CGColor];
        self.bgView.layer.borderWidth = 2;
        
        
        self.firstView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-20, 39)];
        self.firstView.backgroundColor = [UIColor whiteColor];
        [self.bgView addSubview:self.firstView];
     
        
        
        self.nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, (self.bgView.frame.size.width-20)/2, 39)];
        self.nameLabel.backgroundColor = [UIColor whiteColor];
        self.nameLabel.textColor = APP_TEXTCOLOR;
        self.nameLabel.text = @"  文心堂书画社";
        self.nameLabel.font = [UIFont systemFontOfSize:13];
        [self.firstView addSubview:self.nameLabel];
        
        self.classLabel= [[UILabel alloc]initWithFrame:CGRectMake((self.bgView.frame.size.width-20)/2+10, 0, (self.bgView.frame.size.width-20)/2, 39)];
        self.classLabel.backgroundColor = [UIColor whiteColor];
        self.classLabel.textColor = APP_TEXTCOLOR;
        self.classLabel.text = @"书法一班";
        self.classLabel.textAlignment = NSTextAlignmentRight;
        self.classLabel.font = [UIFont systemFontOfSize:13];
        [self.firstView addSubview:self.classLabel];
        
        
        self.phoneView = [[UIView alloc]initWithFrame:CGRectMake(0, 40, APP_VIEW_WIDTH-20, 39)];
        self.phoneView.backgroundColor = [UIColor whiteColor];
        [self.bgView addSubview:self.phoneView];

        
        self.phoneLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, self.bgView.frame.size.width-20, 39)];
        self.phoneLabel.backgroundColor = [UIColor whiteColor];
        self.phoneLabel.textColor = APP_TEXTCOLOR;
        self.phoneLabel.text = @"  咨询电话 : 400 123 456";
        self.phoneLabel.font = [UIFont systemFontOfSize:12];
        [self.phoneView addSubview:self.phoneLabel];
        
        
        self.timeView = [[UIView alloc]initWithFrame:CGRectMake(0, 80, APP_VIEW_WIDTH-20, 39)];
        self.timeView.backgroundColor = [UIColor whiteColor];
        [self.bgView addSubview:self.timeView];
        
        self.timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, self.bgView.frame.size.width-20, 39)];
        self.timeLabel.backgroundColor = [UIColor whiteColor];
        self.timeLabel.textColor = APP_TEXTCOLOR;
        self.timeLabel.text = @"  时间 : 400 123 456";
        self.timeLabel.font = [UIFont systemFontOfSize:12];
        [self.timeView addSubview:self.timeLabel];
        
        
        
        self.replayView = [[UIView alloc]initWithFrame:CGRectMake(0, 120, APP_VIEW_WIDTH-20, 39)];
        self.replayView.backgroundColor = [UIColor whiteColor];
        [self.bgView addSubview:self.replayView];

        
        self.replayLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, self.bgView.frame.size.width-20, 39)];
         self.replayLabel.backgroundColor = [UIColor whiteColor];
         self.replayLabel.textColor = APP_TEXTCOLOR;
         self.replayLabel.text = @"  学校回复 : 131sfasfdasasvasdfasfasfasfdsafdsafasvadvsafdqwefwq";
         self.replayLabel.font = [UIFont systemFontOfSize:12];
        self.replayLabel.numberOfLines = 0;
        [self.replayView addSubview: self.replayLabel];
        
        
        self.feeView = [[UIView alloc]initWithFrame:CGRectMake(0, 160, APP_VIEW_WIDTH-20, 39)];
        self.feeView.backgroundColor = [UIColor whiteColor];
        [self.bgView addSubview:self.feeView];

        self.feeLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, self.bgView.frame.size.width-20, 39)];
        self.feeLabel.backgroundColor = [UIColor clearColor];
        self.feeLabel.textColor = APP_NAVCOLOR;
        self.feeLabel.text = @"   学费:5000元";
        self.feeLabel.font = [UIFont systemFontOfSize:13];
        [self.feeView addSubview:self.feeLabel];
        
        
        self.payButton = [[UIButton alloc]init];
        [self.payButton setTitle:@"缴 费" forState:UIControlStateNormal];
        [self.payButton setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        self.payButton.layer.cornerRadius = 3;
        self.payButton.layer.masksToBounds = YES;
        self.payButton.layer.borderColor = [APP_NAVCOLOR CGColor];
        self.payButton.layer.borderWidth = 0.5;
        self.payButton.titleLabel.font = [UIFont systemFontOfSize:15];
        
        [self.payButton addTarget:self action:@selector(clickPay) forControlEvents:UIControlEventTouchUpInside];
        
        [self.feeView addSubview:self.payButton];
        
        
    }
    return self;
}
-(void)setTrainCell:(NSDictionary *)trainDic{
    
//    NSLog(@"%@",trainDic);
//    self.trainDic = trainDic;
    
    self.nameLabel.text = [trainDic objectForKey:@"shopName"];
    self.classLabel.text = [trainDic objectForKey:@"className"];
    self.phoneLabel.text = [NSString stringWithFormat:@"咨询电话 : %@",[trainDic objectForKey:@"tel"]];
    self.timeLabel.text = [NSString stringWithFormat:@"时间 : %@",[trainDic objectForKey:@"signTime"]];
    self.replayLabel.text =[NSString stringWithFormat:@"学校回复 : %@",[trainDic objectForKey:@"handMemo"]];
    
    
    NSString *str = [NSString stringWithFormat: @"学费 : %@元",[trainDic objectForKey:@"signFee"]];
    NSMutableAttributedString *attStr = [[NSMutableAttributedString alloc]initWithString:str];
    [attStr addAttribute:NSForegroundColorAttributeName value:APP_TEXTCOLOR range:NSMakeRange(0,4)];
    
    self.feeLabel.attributedText =attStr;
    
    
    NSString *handMemo=[NSString stringWithFormat:@"学校回复 : %@",[trainDic objectForKey:@"handMemo"]];
    CGSize size = [handMemo boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-40, MAXFLOAT)
                                         options:NSStringDrawingUsesLineFragmentOrigin
                                      attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12]}
                                         context:nil].size;
    
    
    self.bgView.frame = CGRectMake(10, 0, APP_VIEW_WIDTH-20, 4*40+10 + (size.height>40?size.height+10:40));
    CGRect rect = self.replayView.frame;
    rect.size.height = (size.height>40?size.height+10:40);
    self.replayView.frame = rect;
    self.replayLabel.frame = CGRectMake(10, 0, self.replayView.frame.size.width-20, rect.size.height);
    
    self.feeView.frame = CGRectMake(0, self.replayView.frame.origin.y+self.replayView.frame.size.height+1, self.bgView.frame.size.width, 39) ;
    self.feeLabel.frame = CGRectMake(10, 0, (self.feeView.frame.size.width-20)/2, 39);
    
    self.payButton.frame = CGRectMake(APP_VIEW_WIDTH-APP_VIEW_WIDTH/4-40, 6, APP_VIEW_WIDTH/4, 26);
    
}
-(void)clickPay{
    if ([self.trainDelegate respondsToSelector:@selector(payTrain:)]) {
        [self.trainDelegate payTrain:self.trainDic];
    }
    
}

@end
